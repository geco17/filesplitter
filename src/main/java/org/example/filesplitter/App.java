package org.example.filesplitter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.filesplitter.controller.impl.FileSplitterControllerImpl;
import org.example.filesplitter.gui.FileSplitterGUIController;
import org.example.filesplitter.service.impl.FileSplitterServiceImpl;
import org.example.filesplitter.service.impl.FileValidationServiceImpl;

import java.io.IOException;

public class App extends Application {

    /**
     * Start the file splitter app.
     * @param stage The main stage.
     * @throws IOException
     */
    @Override
    public void start(final Stage stage) throws IOException {
        stage.setTitle("File splitter");
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/main.fxml"));
        final int maxBufferSize = 4096;
        loader.setControllerFactory(clazz -> new FileSplitterGUIController(
                stage,
                new FileSplitterControllerImpl(
                        new FileValidationServiceImpl(),
                        new FileSplitterServiceImpl(maxBufferSize))));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * File splitter app entry point.
     * @param args The application arguments.
     */
    public static void main(final String[] args) {
        launch();
    }

}
