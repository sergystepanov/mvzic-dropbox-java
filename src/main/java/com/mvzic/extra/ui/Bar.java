package com.mvzic.extra.ui;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.event.MessagedEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Bar extends Pane {
    private Label label;

    public Bar() {
        label = new Label();
        this.getChildren().add(label);
    }

    @Subscribe
    public void listenMessage(final MessagedEvent event) {
        label.setText(event.getMessage());
    }
}
