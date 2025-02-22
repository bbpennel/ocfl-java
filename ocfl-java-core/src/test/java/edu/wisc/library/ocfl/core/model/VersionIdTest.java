package edu.wisc.library.ocfl.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VersionIdTest {

    @Test
    public void shouldCreateVersionWhenValidNoPadding() {
        var versionId = VersionId.fromValue("v3");
        assertEquals("v3", versionId.toString());
    }

    @Test
    public void shouldCreateVersionWhenValidWithPadding() {
        var versionId = VersionId.fromValue("v004");
        assertEquals("v004", versionId.toString());
    }

    @Test
    public void shouldFailWhenNoLeadingV() {
        assertThrows(IllegalArgumentException.class, () -> VersionId.fromValue("1"));
    }

    @Test
    public void shouldFailWhenZero() {
        assertThrows(IllegalArgumentException.class, () -> VersionId.fromValue("v0"));
    }

    @Test
    public void shouldFailWhenMultipleZeros() {
        assertThrows(IllegalArgumentException.class, () -> VersionId.fromValue("v00"));
    }

    @Test
    public void shouldFailWhenHasExtraChars() {
        assertThrows(IllegalArgumentException.class, () -> VersionId.fromValue("v1.2"));
    }

    @Test
    public void shouldIncrementVersionWhenNoPadding() {
        var versionId = VersionId.fromValue("v3");
        var nextVersion = versionId.nextVersionId();
        assertEquals("v3", versionId.toString());
        assertEquals("v4", nextVersion.toString());
    }

    @Test
    public void shouldIncrementVersionWhenHasPadding() {
        var versionId = VersionId.fromValue("v03");
        var nextVersion = versionId.nextVersionId();
        assertEquals("v03", versionId.toString());
        assertEquals("v04", nextVersion.toString());
    }

    @Test
    public void shouldDecrementVersionWhenNoPadding() {
        var versionId = VersionId.fromValue("v3");
        var previousVersion = versionId.previousVersionId();
        assertEquals("v3", versionId.toString());
        assertEquals("v2", previousVersion.toString());
    }

    @Test
    public void shouldDecrementVersionWhenHasPadding() {
        var versionId = VersionId.fromValue("v03");
        var previousVersion = versionId.previousVersionId();
        assertEquals("v03", versionId.toString());
        assertEquals("v02", previousVersion.toString());
    }

    @Test
    public void shouldFailDecrementWhenPreviousVersion0() {
        var versionId = VersionId.fromValue("v1");
        assertThrows(IllegalStateException.class, versionId::previousVersionId);
    }

    @Test
    public void shouldFailIncrementWhenNextVersionIsIllegalPaddedNumber() {
        var versionId = VersionId.fromValue("v09");
        assertThrows(IllegalStateException.class, versionId::nextVersionId);
    }

}
