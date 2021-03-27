package org.example.filesplitter.gui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.filesplitter.controller.FileSplitterController;
import org.example.filesplitter.gui.listener.TextChangeListener;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileSplitterGUIController implements FileSplitterController {

    /**
     * Since this controller starts the task in a separate thread, the
     * controller can only return that the task was started.
     */
    public static final int SPLIT_TASK_STARTED = -1;

    /**
     * Decorator pattern: invoke this controller inside the
     * {@link #split(Path, Path, int, AtomicBoolean)} method of
     * this class.
     */
    private final FileSplitterController controller;

    /**
     * The stage the controller is operating in.
     */
    private final Stage stage;

    /**
     * The text field for the chunk size.
     */
    @FXML
    private TextField chunkSize;

    /**
     * A button to open the destination directory chooser.
     */
    @FXML
    private Button destButton;

    /**
     * A button for initiating the split.
     */
    @FXML
    private Button splitButton;

    /**
     * A button to open the source file chooser.
     */
    @FXML
    private Button srcButton;

    /**
     * A textbox for the destination path.
     */
    @FXML
    private TextField dest;

    /**
     * A textbox for the source path.
     */
    @FXML
    private TextField src;

    /**
     * Create the GUI controller with the specified stage and by decorating a
     * base splitter controller.
     *
     * @param stage      The window stage.
     * @param controller The base controller to decorate.
     */
    public FileSplitterGUIController(final Stage stage,
                                     final FileSplitterController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    /**
     * Things to initialize: listener on the fields.
     */
    @FXML
    void initialize() {
        List<TextField> fields = Arrays.asList(src, dest, chunkSize);
        TextChangeListener listener = new TextChangeListener(
                fields, splitButton);
        fields.forEach(f -> f.textProperty().addListener(listener));
    }

    /**
     * Run the split task in a separate thread and allow the user to interrupt
     * it.
     *
     * @param src         The source file.
     * @param dest        The destination directory.
     * @param chunkSize   The size of each chunk.
     * @param interrupted A flag for interrupting from outside the method.
     * @return Always return {@link #SPLIT_TASK_STARTED} (async call).
     */
    @Override
    public int split(final Path src, final Path dest, final int chunkSize,
                     final AtomicBoolean interrupted) {
        Task<Integer> splitTask = new Task<>() {
            @Override
            protected Integer call() {
                return controller.split(src, dest, chunkSize, interrupted);
            }
        };

        Dialog<Void> interrupt = new Dialog<>();
        interrupt.setTitle("Splitting...");
        interrupt.setHeaderText("Splitting...");
        interrupt.setContentText("Press cancel to interrupt");
        interrupt.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        interrupt.setResultConverter(buttonType -> {
            interrupted.set(true);
            splitTask.cancel(true);
            return null;
        });

        splitTask.setOnSucceeded(workerStateEvent -> {
            interrupt.hide();
            showDialog(splitTask.getValue());
        });

        splitTask.setOnCancelled(workerStateEvent -> {
            System.out.println("cancelled");
        });

        interrupt.show();
        new Thread(splitTask).start();
        return SPLIT_TASK_STARTED;
    }

    /**
     * Show a dialog based on the split result.
     *
     * @param result The result returned from the split operation.
     */
    private void showDialog(final int result) {
        if (result == SUCCESS) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("The source file was split successfully.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        String msg;
        switch (result) {
            case INVALID_SOURCE_FILE:
                msg = "Invalid source file: " + src.getText();
                break;
            case INVALID_DESTINATION_DIR:
                msg = "Invalid destination dir: " + dest.getText();
                break;
            case INVALID_CHUNK_SIZE:
                msg = "Invalid chunk size: " + chunkSize.getText();
                break;
            case IO_ERROR:
                msg = "I/O error";
                break;
            default:
                msg = "Generic error";
                break;
        }
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Show the file chooser to get the source file.
     *
     * @param actionEvent The UI event.
     */
    public void srcFileChooser(final ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Source file");
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("All files", "*.*"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            src.setText(file.getAbsolutePath());
        }
    }

    /**
     * Show the directory chooser to get the destination directory.
     *
     * @param actionEvent The UI event.
     */
    public void destDirChooser(final ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Destination directory");
        File file = chooser.showDialog(stage);
        if (file != null) {
            dest.setText(file.getAbsolutePath());
        }
    }

    /**
     * If the chunk size is a numeric value then start the split. Otherwise
     * show an error message.
     *
     * @param actionEvent The action event received from the UI.
     */
    public void splitSrc(final ActionEvent actionEvent) {
        final int bytes;
        try {
            bytes = Integer.parseInt(chunkSize.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Chunk size not valid: "
                    + chunkSize.getText());
            alert.showAndWait();
            return;
        }

        split(Path.of(src.getText()),
                Path.of(dest.getText()),
                bytes,
                new AtomicBoolean(false));
    }

}
