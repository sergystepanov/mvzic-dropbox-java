package com.mvzic.extra.ui;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.ui.DirectorySelectionChangedEvent;
import javafx.scene.control.Button;

/**
 * The directory selection element {@code view}.
 *
 * @since 1.0.0
 */
public final class DirChooserView extends Button {
    private String path;

    /**
     * Constructs a new directory selection element.
     *
     * @param eventBus The event bus reference to pass events outside.
     * @since 1.0.0
     */
    public DirChooserView(final EventBus eventBus) {
        this.setText("Выбрать");
        this.setOnAction(event -> eventBus.post(new DirectorySelectionChangedEvent()));
    }

    public String getPath() {
        return path;
    }
}
