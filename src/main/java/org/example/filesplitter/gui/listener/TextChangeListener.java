package org.example.filesplitter.gui.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.List;

public class TextChangeListener implements ChangeListener<String> {

    /**
     * The text fields that must not be empty.
     */
    private final List<TextField> fields;

    /**
     * The button to enable if all are not empty.
     */
    private final Button target;

    /**
     * Create a text change listener with fields and the button to enable if
     * they're all non-empty.
     *
     * @param fields A list of fields to check.
     * @param target The button to enable if they're all okay.
     */
    public TextChangeListener(final List<TextField> fields,
                              final Button target) {
        this.fields = fields;
        this.target = target;
    }

    /**
     * When text changes, see if the action button can be enabled.
     *
     * @param observableValue The value to observe.
     * @param oldValue        The previous value.
     * @param newValue        The new value.
     */
    @Override
    public void changed(final ObservableValue<? extends String> observableValue,
                        final String oldValue, final String newValue) {
        if (newValue.isEmpty()) {
            target.setDisable(true);
            return;
        }
        for (TextField f : fields) {
            String text = f.getText();
            if (text.isEmpty()) {
                target.setDisable(true);
                return;
            }
        }
        target.setDisable(false);
    }
}
