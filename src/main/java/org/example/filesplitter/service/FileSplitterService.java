package org.example.filesplitter.service;

import org.example.filesplitter.exception.SplitException;

import java.nio.file.Path;

public interface FileSplitterService {

    /**
     * @param src       The path, corresponding to a file to split.
     * @param dest      The destination directory.
     * @param chunkSize The chunk size in bytes (size of each split).
     */
    void split(Path src, Path dest, int chunkSize) throws SplitException;

}
