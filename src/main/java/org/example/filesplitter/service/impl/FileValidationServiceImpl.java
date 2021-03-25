package org.example.filesplitter.service.impl;

import org.example.filesplitter.service.FileValidationService;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidationServiceImpl implements FileValidationService {
    @Override
    public boolean validSrc(final Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    public boolean validDest(final Path path) {
        return Files.isDirectory(path);
    }

    @Override
    public boolean validChunkSize(int chunkSize) {
        return chunkSize > 0;
    }
}
