package com.mvzic.extra.ui;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.event.MessagedEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public final class Bar extends Pane {
    private final Label label;

    public Bar() {
        label = new Label();
        getChildren().add(label);
    }

    @Subscribe
    void onMessage(final MessagedEvent event) {
        label.setText(event.getMessage());
    }
}
