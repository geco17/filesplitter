package org.example.filesplitter.service.impl;

import org.example.filesplitter.Support;
import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FileSplitterServiceImplTest {

    private final int bufferSize = 10;

    private final FileSplitterService service = new FileSplitterServiceImpl(bufferSize);

    @TempDir
    static Path tempDir;

    static Path tempFile;

    static final byte[] bytes = new byte[10];

    static {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
    }

    @BeforeAll
    static void init() {
        tempFile = new Support(tempDir).createFile("test.txt");
        try {
            Files.write(tempFile, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFailOnBufferSizeZero() {
        assertThrows(IllegalStateException.class, () -> new FileSplitterServiceImpl(0));
    }

    @Test
    public void testFailOnNegativeBufferSize() {
        assertThrows(IllegalStateException.class, () -> new FileSplitterServiceImpl(-10));
    }

    @Test
    public void testSuccessWithChunkSizeEqualToMaxBuffer() throws SplitException, IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out1"));
        service.split(tempFile, dest, 10, new AtomicBoolean(false));
        List<Path> paths = Files.list(dest).collect(Collectors.toList());
        assertEquals(1, paths.size());
        byte[] content = Files.readAllBytes(paths.get(0));
        assertArrayEquals(bytes, content);
    }

    @Test
    public void testSuccessWithChunkSizeGreaterThanMaxBufferAndContentLengthMaxBuffer() throws SplitException, IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out2"));
        service.split(tempFile, dest, 100, new AtomicBoolean(false));
        List<Path> paths = Files.list(dest).collect(Collectors.toList());
        assertEquals(1, paths.size());
        byte[] content = Files.readAllBytes(paths.get(0));
        assertArrayEquals(bytes, content);
    }

    @Test
    public void testSuccessWithChunkSizeGreaterThanMaxBufferAndContentLengthMoreThanMaxBuffer() throws SplitException, IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out3"));
        FileSplitterService service = new FileSplitterServiceImpl(2);
        service.split(tempFile, dest, 100, new AtomicBoolean(false));
        List<Path> paths = Files.list(dest).collect(Collectors.toList());
        assertEquals(1, paths.size());
        byte[] content = Files.readAllBytes(paths.get(0));
        assertArrayEquals(bytes, content);
    }

    @Test
    public void testSuccessWithChunkSizeGreaterThanMaxBufferAndContentLengthLessThanMaxBuffer() throws SplitException, IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out4"));
        FileSplitterService service = new FileSplitterServiceImpl(12);
        service.split(tempFile, dest, 100, new AtomicBoolean(false));
        List<Path> paths = Files.list(dest).collect(Collectors.toList());
        assertEquals(1, paths.size());
        byte[] content = Files.readAllBytes(paths.get(0));
        assertArrayEquals(bytes, content);
    }

    @Test
    public void testSuccessWithChunkSizeLessThanMaxBuffer() throws SplitException, IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out5"));
        service.split(tempFile, dest, 2, new AtomicBoolean(false));
        List<Path> paths = Files.list(dest).collect(Collectors.toList());
        assertEquals(5, paths.size());
        int curChunkIndex = 0;
        byte[] part = new byte[2];
        for (Path p : paths) {
            System.arraycopy(bytes, curChunkIndex, part, 0, 2);
            byte[] content = Files.readAllBytes(p);
            assertArrayEquals(part, content);
            curChunkIndex += 2;
        }
    }

    @Test
    public void testCatchExceptionOnWriteFail() throws IOException {
        Path dest = Files.createDirectory(tempDir.resolve("out6"));
        try {
            service.split(null, dest, 10, new AtomicBoolean(false));
        } catch (SplitException e) {
            return;
        }
        fail("Exception not caught");
    }

}
