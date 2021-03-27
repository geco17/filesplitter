package org.example.filesplitter.service;

import org.example.filesplitter.exception.SplitException;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public interface FileSplitterService {

    /**
     * @param src         The path, corresponding to a file to split.
     * @param dest        The destination directory.
     * @param chunkSize   The chunk size in bytes (size of each split).
     * @param interrupted A flag for interrupting from outside the method.
     */
    void split(Path src, Path dest, int chunkSize, AtomicBoolean interrupted)
            throws SplitException;

}
