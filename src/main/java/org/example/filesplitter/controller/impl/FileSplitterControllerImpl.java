package org.example.filesplitter.controller.impl;

import org.example.filesplitter.controller.FileSplitterController;
import org.example.filesplitter.exception.SplitException;
import org.example.filesplitter.service.FileSplitterService;
import org.example.filesplitter.service.FileValidationService;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSplitterControllerImpl implements FileSplitterController {

    private static final Logger LOGGER = Logger.getLogger(FileSplitterControllerImpl.class.getName());

    private final FileValidationService fileValidationService;

    private final FileSplitterService fileSplitterService;

    public FileSplitterControllerImpl(FileValidationService fileValidationService, FileSplitterService fileSplitterService) {
        this.fileValidationService = fileValidationService;
        this.fileSplitterService = fileSplitterService;
    }

    @Override
    public int split(Path src, Path dest, int chunkSize) {
        if (!fileValidationService.validSrc(src)) {
            LOGGER.log(Level.SEVERE, "[abort] source file not valid: " + src);
            return 1;
        }
        if (!fileValidationService.validDest(dest)) {
            LOGGER.log(Level.SEVERE, "[abort] destination directory not valid: " + dest);
            return 2;
        }
        if (!fileValidationService.validChunkSize(chunkSize)) {
            LOGGER.log(Level.SEVERE, "[abort] chunk size (bytes) not valid: " + chunkSize);
            return 3;
        }
        try {
            fileSplitterService.split(src, dest, chunkSize);
        } catch (SplitException e) {
            LOGGER.log(Level.SEVERE, "[error] file split error: ", e);
            return 4;
        }
        return 0;
    }
}
