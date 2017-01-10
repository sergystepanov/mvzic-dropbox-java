package com.mvzic.extra.ui;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.ui.LoadingEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.concurrent.ScheduledExecutorService;

/**
 * The task bar UI element.
 *
 * @since 1.0.0
 */
public final class Bar extends Pane {
    private final LoadIndicator loading;
    private final Label label;

    public Bar(final ScheduledExecutorService scheduler) {
        loading = new LoadIndicator(scheduler, 60.0, 1.0);
        label = new Label();
        getChildren().addAll(new HBox(1, loading.getNode(), label));
    }

    @Subscribe
    void onMessage(final MessagedEvent event) {
        label.setText(event.getMessage());
    }

    @Subscribe
    void onLoad(final LoadingEvent event) {
        loading.set(event.getState());
    }
}
