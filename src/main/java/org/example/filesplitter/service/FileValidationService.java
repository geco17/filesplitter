package org.example.filesplitter.service;

import java.nio.file.Path;

public interface FileValidationService {

    /**
     * Is this path a valid source file?
     *
     * @param path The path to check.
     * @return true if the source is a file and exists, false otherwise.
     */
    boolean validSrc(Path path);

    /**
     * Is this path a valid destination directory?
     *
     * @param path The path to check.
     * @return true if the source is a directory and exists, false otherwise.
     */
    boolean validDest(Path path);

    /**
     * Is the chunk size (in bytes), the size of the split, valid?
     *
     * @param chunkSize The chunk size to check.
     * @return true if it's greater than 0, false otherwise.
     */
    boolean validChunkSize(int chunkSize);
}
