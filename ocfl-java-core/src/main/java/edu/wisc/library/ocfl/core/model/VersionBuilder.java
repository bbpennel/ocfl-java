package edu.wisc.library.ocfl.core.model;

import edu.wisc.library.ocfl.api.model.CommitInfo;
import edu.wisc.library.ocfl.api.util.Enforce;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Used to construct Version objects.
 */
public class VersionBuilder {

    private OffsetDateTime created;
    private String message;
    private User user;
    private Map<String, Set<String>> state;
    private Map<String, String> reverseStateMap;

    public VersionBuilder() {
        state = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        reverseStateMap = new HashMap<>();
    }

    /**
     * Used to construct a new Version that's based on an existing version. The existing version's state is copied over
     * to the new version.
     *
     * @param original
     */
    public VersionBuilder(Version original) {
        Enforce.notNull(original, "version cannot be null");
        state = original.getMutableState();
        reverseStateMap = original.getMutableReverseStateMap();
    }

    public String getFileId(String logicalPath) {
        return reverseStateMap.get(logicalPath);
    }

    public boolean containsFileId(String fileId) {
        return state.containsKey(fileId);
    }

    public VersionBuilder removePath(String logicalPath) {
        var id = reverseStateMap.remove(logicalPath);

        if (id != null) {
            var paths = state.get(id);
            if (paths.size() == 1) {
                state.remove(id);
            } else {
                paths.remove(logicalPath);
            }
        }

        return this;
    }

    /**
     * The logicalPath field should be relative to the content directory and not the inventory root.
     */
    public VersionBuilder addFile(String id, String logicalPath) {
        Enforce.notBlank(id, "id cannot be blank");
        Enforce.notBlank(logicalPath, "logicalPath cannot be blank");

        state.computeIfAbsent(id, k -> new HashSet<>()).add(logicalPath);
        reverseStateMap.put(logicalPath, id);
        return this;
    }

    public VersionBuilder created(OffsetDateTime created) {
        this.created = created;
        return this;
    }

    public VersionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public VersionBuilder user(User user) {
        this.user = user;
        return this;
    }

    public VersionBuilder commitInfo(CommitInfo commitInfo) {
        if (commitInfo != null) {
            this.message = commitInfo.getMessage();
            if (commitInfo.getUser() != null) {
                this.user = new User(commitInfo.getUser().getName(), commitInfo.getUser().getAddress());
            }
        }
        return this;
    }

    public VersionBuilder state(Map<String, Set<String>> state) {
        this.state = state;
        return this;
    }

    public VersionBuilder reverseStateMap(Map<String, String> reverseStateMap) {
        this.reverseStateMap = reverseStateMap;
        return this;
    }

    public Version build() {
        return new Version(created, message, user, state, reverseStateMap);
    }

}
