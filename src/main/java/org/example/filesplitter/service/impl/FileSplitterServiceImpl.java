package org.example.filesplitter.service.impl;

import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileSplitterServiceImpl implements FileSplitterService {

    private final int maxBufferSize;

    public FileSplitterServiceImpl(int maxBufferSize) {
        if (maxBufferSize <= 0) {
            throw new IllegalStateException("Max buffer size not valid: " + maxBufferSize);
        }
        this.maxBufferSize = maxBufferSize;
    }

    @Override
    public void split(Path src, Path dest, int chunkSize) throws SplitException {
        final byte[] buffer = buffer(chunkSize);
        try (InputStream is = new FileInputStream(src.toFile())) {
            int bytesRead;
            long chunkBytesRead = 0;
            long count = 0;
            String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmssSSSSSSSSS"));
            while ((bytesRead = is.read(buffer)) > 0) {
                chunkBytesRead += bytesRead;
                writeBytes(dest, String.format("%s_split_%d", start, count), buffer, bytesRead);
                if (chunkBytesRead % chunkSize == 0) { // finished this chunk
                    count++;
                    chunkBytesRead = 0;
                }
            }
        } catch (Exception e) {
            throw new SplitException(e);
        }
    }

    private void writeBytes(Path dest, String filename, byte[] buffer, int bytesRead) throws IOException {
        File destFile = Path.of(
                dest.toFile().getAbsolutePath(), filename).toFile();
        try (OutputStream out = new FileOutputStream(destFile.getAbsoluteFile(), true)) {
            out.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Get a buffer based on the chunk size.
     *
     * @param chunkSize The size in bytes of each chunk.
     * @return The byte[] buffer.
     */
    private byte[] buffer(int chunkSize) {
        if (chunkSize < maxBufferSize) {
            return new byte[chunkSize];
        } else {
            return new byte[maxBufferSize];
        }
    }
}
