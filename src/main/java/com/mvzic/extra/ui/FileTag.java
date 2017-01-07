package com.mvzic.extra.ui;

import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.text.TextAlignment;

/**
 * The file tag class.
 *
 * @since 1.0.0
 */
public final class FileTag extends Label {
    public FileTag(final String text) {
        super(text);
        setTextAlignment(TextAlignment.CENTER);
        setStyle("-fx-padding: 0 .1em;" +
                "-fx-min-width: 2em;"+
                " -fx-background-color: lightgray;" +
                "-fx-font-size: 70%;" +
                "-fx-font-weight: bold;");
    }
}
