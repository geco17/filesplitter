package org.example.filesplitter;

import org.example.filesplitter.controller.FileSplitterController;
import org.example.filesplitter.controller.impl.FileSplitterControllerImpl;
import org.example.filesplitter.service.impl.FileSplitterServiceImpl;
import org.example.filesplitter.service.impl.FileValidationServiceImpl;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        final Path src;
        final Path dest;
        final int chunkSize;
        final int bufferSize;
        try {
            src = Path.of(args[0]);
            dest = Path.of(args[1]);
            chunkSize = Integer.parseInt(args[2]);
            bufferSize = Integer.parseInt(args[3]);
        }  catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Illegal arguments specified");
            LOGGER.log(Level.INFO, "Invoke with source path, destination path, chunk size, buffer size");
            return;
        }
        FileSplitterController controller = new FileSplitterControllerImpl(
                new FileValidationServiceImpl(),
                new FileSplitterServiceImpl(bufferSize));
        int result = controller.split(src, dest, chunkSize);
        LOGGER.log(Level.INFO, "EXit code: " + result);
    }

}
