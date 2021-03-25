package org.example.filesplitter.service.impl;

import org.example.filesplitter.Support;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileValidationServiceImplTest {

    private final FileValidationServiceImpl service = new FileValidationServiceImpl();

    @TempDir
    static Path tempDir;

    static Path validSrc;

    static Path validDest;

    @BeforeAll
    public static void init() {
        validSrc = new Support(tempDir).createFile("test.txt");
        System.out.println("Created file for valid source tests: " + validSrc);
        try {
            validDest = Files.createTempDirectory(tempDir, "test-dest-dir");
        } catch (IOException e) {
            throw new RuntimeException("Could not create temp dest directory", e);
        }
        System.out.println("Created directory for valid destination tests: " + validSrc);
    }

    /**
     * If the source exists and is a file (not directory), the expected result of
     * {@link FileValidationServiceImpl#validSrc(Path)} is true.
     */
    @Test
    public void testSourceExistsAndIsFile() {
        assertTrue(service.validSrc(validSrc));
    }

    /**
     * If the destination exists and is a directory, the expected result of
     * {@link FileValidationServiceImpl#validDest(Path)} is true.
     */
    @Test
    public void testDestinationExistsAndIsDirectory() {
        assertTrue(service.validDest(validDest));
    }

    /**
     * If the source exists and is a directory (not a regular file), the expected result of
     * {@link FileValidationServiceImpl#validSrc(Path)} is false.
     */
    @Test
    public void testSourceExistsAndIsDirectory() {
        assertFalse(service.validSrc(tempDir));
    }

    /**
     * If the destination exists and is a file (not a directory), the expected result of
     * {@link FileValidationServiceImpl#validDest(Path)} is false. In this test {@link #validSrc} is used since it's
     * a regular file.
     */
    @Test
    public void testDestinationExistsAndIsFile() {
        assertFalse(service.validDest(validSrc));
    }

    /**
     * If the destination does not exist, the expected result of {@link FileValidationServiceImpl#validDest(Path)} is
     * false.
     */
    @Test
    public void testDestinationDoesNotExist() {
        Path path;
        String prefix = "subdir";
        try {
            path = Files.createTempDirectory(tempDir, prefix);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "Could not create temp directory with prefix %s in directory %s",
                            prefix,
                            tempDir.toFile().getAbsolutePath()));
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete temp directory: " + path.toFile().getAbsolutePath());
        }
        assertFalse(service.validDest(path));
    }

    /**
     * If the source does not exist, the expected result of {@link FileValidationServiceImpl#validSrc(Path)} is false.
     */
    @Test
    public void testSourceDoesNotExist() {
        String prefix = "tempfile";
        String suffix = ".txt";
        Path path;
        try {
            path = Files.createTempFile(tempDir, prefix, suffix);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "Could not create temp file to delete with prefix %s and suffix %s",
                            prefix,
                            suffix),
                    e);
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete temp file: " + path.toFile().getAbsolutePath());
        }
        assertFalse(service.validSrc(path));
    }

    /**
     * If the chunk size is equal to zero, the expected result of
     * {@link FileValidationServiceImpl#validChunkSize(int)} is false.
     */
    @Test
    public void testChunkSizeZero() {
        assertFalse(service.validChunkSize(0));
    }

    /**
     * If the chunk size is less than zero, the expected result of
     * {@link FileValidationServiceImpl#validChunkSize(int)} is false.
     */
    @Test
    public void testChunkSizeLessThanZero() {
        assertFalse(service.validChunkSize(-1));
    }

    /**
     * If the chunk size is greater than zero the expected result of
     * {@link FileValidationServiceImpl#validChunkSize(int)} is true.
     */
    @Test
    public void testChunkSizeMoreThanZero() {
        assertTrue(service.validChunkSize(1));
    }

}
