package org.example.filesplitter.controller;

import java.nio.file.Path;

public interface FileSplitterController {

    /**
     * Split the source file into chunks of the specified size, saving each chunk in the destination folder.
     *
     * @param src       The source file.
     * @param dest      The destination directory.
     * @param chunkSize The size of each chunk.
     * @return the result of the operation. Exit code 0 is no error.
     */
    int split(final Path src, final Path dest, final int chunkSize);

}
