package org.example.filesplitter.service.impl;

import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileSplitterServiceImpl implements FileSplitterService {

    /**
     * The max buffer size allowed.
     */
    private final int maxBufferSize;

    /**
     * Create the service with the specified max buffer size in bytes.
     *
     * @param maxBufferSize The max buffer size with which to create the
     *                      service.
     */
    public FileSplitterServiceImpl(final int maxBufferSize) {
        if (maxBufferSize <= 0) {
            throw new IllegalStateException(
                    "Max buffer size not valid: " + maxBufferSize);
        }
        this.maxBufferSize = maxBufferSize;
    }

    /**
     * Split the file, reading in one "chunk" of bytes at a time and writing it
     * with a filename like uuuuMMdd'T'HHmmssSSSSSSSSS_split_N, where N is the
     * chunk number, starting from 1.
     *
     * @param src         The path, corresponding to a file to split.
     * @param dest        The destination directory.
     * @param chunkSize   The chunk size in bytes (size of each split).
     * @param interrupted A flag for interrupting from outside the method.
     * @throws SplitException
     */
    @Override
    public void split(final Path src, final Path dest, final int chunkSize,
                      final AtomicBoolean interrupted) throws SplitException {
        final byte[] buffer = buffer(chunkSize);
        try (InputStream is = new FileInputStream(src.toFile())) {
            int bytesRead;
            long chunkBytesRead = 0;
            long count = 1;
            String start = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmssSSSSSSSSS"));
            while (!interrupted.get() && (bytesRead = is.read(buffer)) > 0) {
                chunkBytesRead += bytesRead;
                writeBytes(dest,
                        String.format("%s_split_%d", start, count),
                        buffer,
                        bytesRead);
                if (chunkBytesRead % chunkSize == 0) { // finished this chunk
                    count++;
                    chunkBytesRead = 0;
                }
            }
        } catch (Exception e) {
            throw new SplitException(e);
        }
    }

    private void writeBytes(final Path dest,
                            final String filename,
                            final byte[] buffer,
                            final int bytesRead) throws IOException {
        File destFile = Path.of(
                dest.toFile().getAbsolutePath(), filename).toFile();
        try (OutputStream out = new FileOutputStream(
                destFile.getAbsoluteFile(),
                true)) {
            out.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Get a buffer based on the chunk size.
     *
     * @param chunkSize The size in bytes of each chunk.
     * @return The byte[] buffer.
     */
    private byte[] buffer(final int chunkSize) {
        if (chunkSize < maxBufferSize) {
            return new byte[chunkSize];
        } else {
            return new byte[maxBufferSize];
        }
    }
}
