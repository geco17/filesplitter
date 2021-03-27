package org.example.filesplitter.controller;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public interface FileSplitterController {

    /**
     * Status code - successful split, no errors.
     */
    int SUCCESS = 0;

    /**
     * Error code - invalid source file.
     */
    int INVALID_SOURCE_FILE = 1;

    /**
     * Error code - the destination directory is not valid.
     */
    int INVALID_DESTINATION_DIR = 2;

    /**
     * Error code - the "chunk size" is not valid. For example, maybe it was
     * zero or a negative number.
     */
    int INVALID_CHUNK_SIZE = 3;

    /**
     * Error code - read / write failure.
     */
    int IO_ERROR = 4;

    /**
     * Split the source file into chunks of the specified size, saving each
     * chunk in the destination folder.
     *
     * @param src         The source file.
     * @param dest        The destination directory.
     * @param chunkSize   The size of each chunk.
     * @param interrupted A flag for interrupting from outside the method.
     * @return the result of the operation. Exit code 0 is no error.
     */
    int split(Path src, Path dest, int chunkSize, AtomicBoolean interrupted);

}
