package org.example.filesplitter.service.impl;

import org.example.filesplitter.service.FileValidationService;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidationServiceImpl implements FileValidationService {

    /**
     * The source file is valid if it's a regular file.
     * @param path The path to check.
     * @return true if valid, false othewise.
     */
    @Override
    public boolean validSrc(final Path path) {
        return Files.isRegularFile(path);
    }

    /**
     * The destination file is valid if it's a directory.
     * @param path The path to check.
     * @return true if valid, false otherwise.
     */
    @Override
    public boolean validDest(final Path path) {
        return Files.isDirectory(path);
    }

    /**
     * The chunk size is valid if it's more than 0.
     * @param chunkSize The chunk size to check.
     * @return true if valid, false otherwise.
     */
    @Override
    public boolean validChunkSize(final int chunkSize) {
        return chunkSize > 0;
    }
}
