package org.example.filesplitter.controller.impl;

import org.example.filesplitter.controller.FileSplitterController;
import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;
import org.example.filesplitter.service.FileValidationService;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileSplitterControllerImplTest {

    private final FileSplitterService fileSplitterService = mock(FileSplitterService.class);

    private final FileValidationService fileValidationService = mock(FileValidationService.class);

    private final FileSplitterController controller = new FileSplitterControllerImpl(
            fileValidationService,
            fileSplitterService);

    private static final Path src = mock(Path.class);

    private static final Path dest = mock(Path.class);

    /**
     * If the source file is not a valid file, return 1.
     */
    @Test
    public void testSourceFileIsNotValid() {
        when(fileValidationService.validSrc(src)).thenReturn(false);
        int actual = controller.split(src, dest, 100);
        assertEquals(1, actual);
    }

    /**
     * If the source file is valid but the destination file is not valid, return 2.
     */
    @Test
    public void testDestFileIsFile() {
        when(fileValidationService.validSrc(src)).thenReturn(true);
        when(fileValidationService.validDest(dest)).thenReturn(false);
        int actual = controller.split(src, dest, 100);
        assertEquals(2, actual);
    }


    /**
     * If the source and destination files are valid but chunk size is invalid, return 3.
     */
    @Test
    public void testChunkSizeInvalid() {
        when(fileValidationService.validSrc(src)).thenReturn(true);
        when(fileValidationService.validDest(dest)).thenReturn(true);
        int actual = controller.split(src, dest, -100);
        assertEquals(3, actual);
    }

    /**
     * If the parameters are all valid but the {@link #fileSplitterService} throws an exception, return 4.
     *
     * @throws SplitException The file splitter service throws a split exception if an error occurs.
     */
    @Test
    public void testSplitServiceFail() throws SplitException {
        when(fileValidationService.validSrc(src)).thenReturn(true);
        when(fileValidationService.validDest(dest)).thenReturn(true);
        int chunkSize = 100;
        when(fileValidationService.validChunkSize(chunkSize)).thenReturn(true);
        doThrow(SplitException.class).when(fileSplitterService).split(src, dest, chunkSize);
        int actual = controller.split(src, dest, chunkSize);
        assertEquals(4, actual);
    }

    /**
     * If all parameters are valid and no exception occurs then return 0.
     */
    @Test
    public void testSplitServicePass() {
        when(fileValidationService.validSrc(src)).thenReturn(true);
        when(fileValidationService.validDest(dest)).thenReturn(true);
        int chunkSize = 100;
        when(fileValidationService.validChunkSize(chunkSize)).thenReturn(true);
        int actual = controller.split(src, dest, chunkSize);
        assertEquals(0, actual);
    }

}
