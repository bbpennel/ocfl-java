package edu.wisc.library.ocfl.api;

import edu.wisc.library.ocfl.api.exception.NotFoundException;
import edu.wisc.library.ocfl.api.exception.ObjectOutOfSyncException;
import edu.wisc.library.ocfl.api.model.CommitInfo;
import edu.wisc.library.ocfl.api.model.ObjectDetails;
import edu.wisc.library.ocfl.api.model.ObjectId;
import edu.wisc.library.ocfl.api.model.VersionDetails;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interface for interacting with an OCFL repository.
 */
public interface OcflRepository {

    /**
     * Adds the object rooted at the given path to the OCFL repository under the given objectId. If their is an existing
     * object with the id, then a new version of the object is created.
     *
     * <p>It is important to note that the files present in the given path should comprise the entirety of the object. Files
     * from previous versions that are no longer present in the path are not carried over. At the same time, files that
     * are unchanged between object versions are not stored a second time.
     *
     * <p>This method should only be used when writing new or fully composed objects.
     *
     * <p>If the current HEAD version of the object does not match the version specified in the request, the update will
     * be rejected. If the request specifies the HEAD version, then no version check will be preformed.
     *
     * <p>By default, files are copied into the OCFL repository. If {@code OcflOption.MOVE_SOURCE} is specified, then
     * files will be moved instead. Warning: If an exception occurs and the new version is not created, the files that were
     * will be lost. This operation is more efficient but less safe than the default copy.
     *
     * @param objectId the id to store the object under. If set to a specific version, then the update will only occur
     *                 if this version matches the head object version in the repository.
     * @param path the path to the object content
     * @param commitInfo information about the changes to the object. Can be null.
     * @param ocflOptions optional config options. Use {@code OcflOption.MOVE_SOURCE} to move files into the repo instead of copying.
     * @return The objectId and version of the new object version
     * @throws ObjectOutOfSyncException when the object was modified by another process before these changes could be committed
     */
    ObjectId putObject(ObjectId objectId, Path path, CommitInfo commitInfo, OcflOption... ocflOptions);

    /**
     * Updates an existing object OR create a new object by selectively adding, removing, moving files within the object,
     * and creating a new version that encapsulates all of the changes. It always operates on the HEAD version of an object,
     * but a specific version can be specified to ensure no intermediate changes were made to the object.
     *
     * <p>If the current HEAD version of the object does not match the version specified in the request, the update will
     * be rejected. If the request specifies the HEAD version, then no version check will be preformed.
     *
     * @param objectId the id of the object. If set to a specific version, then the update will only occur
     *                 if this version matches the head object version in the repository.
     * @param commitInfo information about the changes to the object. Can be null.
     * @param objectUpdater code block within which updates to an object may be made
     * @return The objectId and version of the new object version
     * @throws NotFoundException when no object can be found for the specified objectId
     * @throws ObjectOutOfSyncException when the object was modified by another process before these changes could be committed
     */
    ObjectId updateObject(ObjectId objectId, CommitInfo commitInfo, Consumer<OcflObjectUpdater> objectUpdater);

    /**
     * Returns the entire contents of the object at the specified version. The outputPath MUST exist, MUST be a directory,
     * and SHOULD be empty. The contents of outputPath will be overwritten.
     *
     * @param objectId the id and version of an object to retrieve
     * @param outputPath the directory to write the object files to
     * @throws NotFoundException when no object can be found for the specified objectId
     */
    void getObject(ObjectId objectId, Path outputPath);

    /**
     * Returns a map of {@code OcflFileRetriever} objects that are used to lazy-load object files. The map keys are the
     * logical file paths of all of the files in the specified version of the object.
     *
     * @param objectId the id and version of an object to retrieve
     * @return a map of {@code OcflFileRetriever} objects keyed off the logical file paths of all of the files in the object
     * @throws NotFoundException when no object can be found for the specified objectId
     */
    Map<String, OcflFileRetriever> getObjectStreams(ObjectId objectId);

    /**
     * Opens an object to access individual files within the object without retrieving everything.
     *
     * @param objectId the id and version of an object to retrieve
     * @param objectReader coe block within which object files can be accessed
     * @throws NotFoundException when no object can be found for the specified objectId
     */
    void readObject(ObjectId objectId, Consumer<OcflObjectReader> objectReader);

    /**
     * Returns all of the details about an object and all of its versions.
     *
     * @param objectId the id of the object to describe
     * @return details about the object
     * @throws NotFoundException when no object can be found for the specified objectId
     */
    ObjectDetails describeObject(String objectId);

    /**
     * Returns the details about a specific version of an object.
     *
     * @param objectId the id and version of the object to describe
     * @return details about the object version
     * @throws NotFoundException when no object can be found for the specified objectId
     */
    VersionDetails describeVersion(ObjectId objectId);

    /**
     * Returns true if an object with the specified id exists in the repository.
     *
     * @param objectId the id of the object
     * @return true if the object exists and false otherwise
     */
    boolean containsObject(String objectId);

    /**
     * Permanently removes an object from the repository. Objects that have been purged are NOT recoverable. If an object
     * with the specified id cannot be found it is considered purged and no exception is thrown.
     *
     * @param objectId the id of the object to purge
     */
    void purgeObject(String objectId);

    // TODO rollbackObject?

    // TODO list objects? this is a daunting prospect without an index

}
