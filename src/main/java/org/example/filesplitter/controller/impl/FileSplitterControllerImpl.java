package org.example.filesplitter.controller.impl;

import org.example.filesplitter.controller.FileSplitterController;
import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;
import org.example.filesplitter.service.FileValidationService;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSplitterControllerImpl implements FileSplitterController {

    /**
     * A simple text logger, nothing fancy.
     */
    private static final Logger LOGGER =
            Logger.getLogger(FileSplitterControllerImpl.class.getName());

    /**
     * The service for validating inputs before moving forward with the split.
     */
    private final FileValidationService fileValidationService;

    /**
     * The service to actually split the file.
     */
    private final FileSplitterService fileSplitterService;

    /**
     * Instantiate a new controller with the necessary services.
     *
     * @param fileValidationService An implementation for validation as
     *                              required.
     * @param fileSplitterService   An implementation for splitting.
     */
    public FileSplitterControllerImpl(
            final FileValidationService fileValidationService,
            final FileSplitterService fileSplitterService) {
        this.fileValidationService = fileValidationService;
        this.fileSplitterService = fileSplitterService;
    }

    /**
     * The flag for interrupting is passed directly to the service.
     * @param src         The source file.
     * @param dest        The destination directory.
     * @param chunkSize   The size of each chunk.
     * @param interrupted A flag for interrupting from outside the method.
     * @return
     */
    @Override
    public int split(final Path src, final Path dest, final int chunkSize,
                     final AtomicBoolean interrupted) {
        if (!fileValidationService.validSrc(src)) {
            LOGGER.log(Level.SEVERE, "[abort] source file not valid: " + src);
            return INVALID_SOURCE_FILE;
        }
        if (!fileValidationService.validDest(dest)) {
            LOGGER.log(Level.SEVERE,
                    "[abort] destination directory not valid: " + dest);
            return INVALID_DESTINATION_DIR;
        }
        if (!fileValidationService.validChunkSize(chunkSize)) {
            LOGGER.log(Level.SEVERE,
                    "[abort] chunk size (bytes) not valid: " + chunkSize);
            return INVALID_CHUNK_SIZE;
        }
        try {
            fileSplitterService.split(src, dest, chunkSize, interrupted);
        } catch (SplitException e) {
            LOGGER.log(Level.SEVERE, "[error] file split error: ", e);
            return IO_ERROR;
        }
        return SUCCESS;
    }
}
