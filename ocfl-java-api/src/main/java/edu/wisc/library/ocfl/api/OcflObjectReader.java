package edu.wisc.library.ocfl.api;

import edu.wisc.library.ocfl.api.exception.OverwriteException;
import edu.wisc.library.ocfl.api.io.FixityCheckInputStream;
import edu.wisc.library.ocfl.api.model.VersionDetails;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Exposes methods for selectively reading files from a specific object at a specific version.
 */
public interface OcflObjectReader {

    /**
     * Returns details about the composition of the object at the current version.
     *
     * @return details about the object version
     */
    VersionDetails describeVersion();

    /**
     * Returns a list of all of the logical paths of all of the files within the object at the current version.
     *
     * @return list of all files in an object
     */
    Collection<String> listFiles();

    /**
     * Retrieves the file at the sourcePath from the OCFL object and writes it to the destinationPath, outside of the OCFL repository.
     * Use {@code OcflOption.OVERWRITE} to overwrite the destination path if it already exists.
     *
     * @param sourcePath the logical path to the file to retrieve from the object
     * @param destinationPath the destination to write the file to, including the file name
     * @param ocflOptions optional config options. Use {@code OcflOption.OVERWRITE} to overwrite existing files within
     *                    an object
     * @throws OverwriteException if there is already a file at the destinationPath and {@code OcflOption.OVERWRITE} was
     *                            not specified
     */
    OcflObjectReader getFile(String sourcePath, Path destinationPath, OcflOption... ocflOptions);

    /**
     * Retrieves the specified file and writes it to an {@code InputStream}.
     *
     * <p>Important: The caller MUST close the InputStream when they are done with it. Additionally, the caller may call
     * {@code checkFixity()} on the stream after all of the data has streamed to ensure the fixity of the data.
     *
     * @param sourcePath the logical path to the file to retrieve from the object
     * @return InputStream containing the file's content
     */
    FixityCheckInputStream getFile(String sourcePath);

}
