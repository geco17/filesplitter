module filesplitter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    exports org.example.filesplitter;
    opens org.example.filesplitter.gui to javafx.fxml;
}