package edu.wisc.library.ocfl.core;

import edu.wisc.library.ocfl.core.model.DigestAlgorithm;
import edu.wisc.library.ocfl.core.model.InventoryType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public final class OcflConstants {

    private OcflConstants() {

    }

    public static final String OCFL_VERSION = "ocfl_1.0";
    public static final String OCFL_OBJECT_VERSION = "ocfl_object_1.0";

    public static final String INVENTORY_FILE = "inventory.json";

    public static final String DEFAULT_INITIAL_VERSION_ID = "v1";
    public static final String DEFAULT_CONTENT_DIRECTORY = "content";
    public static final InventoryType DEFAULT_INVENTORY_TYPE = InventoryType.OCFL_1_0;

    public static final DigestAlgorithm DEFAULT_DIGEST_ALGORITHM = DigestAlgorithm.sha512;
    public static final Set<DigestAlgorithm> ALLOWED_DIGEST_ALGORITHMS = Set.of(DigestAlgorithm.sha512, DigestAlgorithm.sha256);

    // TODO move this someplace else?
    public static final Path MUTABLE_HEAD_EXT_PATH = Paths.get("extensions/mutable-head");
    public static final Path MUTABLE_HEAD_VERSION_PATH = MUTABLE_HEAD_EXT_PATH.resolve("HEAD");

}
