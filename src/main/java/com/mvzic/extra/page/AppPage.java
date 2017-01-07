package com.mvzic.extra.page;

import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.lang.UnicodeBundle;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

/**
 * Default application page.
 *
 * @since 1.0.0
 */
@Slf4j
public abstract class AppPage extends Pane {
    private final WatchedEventBus eventBus;
    private final UnicodeBundle unicodeBundle;

    /**
     * Initializes a new {@code Pane}.
     *
     * @param eventBus The event bus reference to pass events outside.
     * @param lang     The language bundle reference to set language dependent values in a {@code View}.
     * @since 1.0.0
     */
    AppPage(final WatchedEventBus eventBus, final UnicodeBundle lang) {
        this.eventBus = eventBus;
        this.unicodeBundle = lang;
    }

    public abstract String getKey();

    WatchedEventBus getEventBus() {
        return eventBus;
    }

    public UnicodeBundle getUnicodeBundle() {
        return unicodeBundle;
    }

    void message(final String message) {
        Platform.runLater(() -> {
            getEventBus().post(new MessagedEvent(message));
            log.info("[m] {}", message);
        });
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && ((AppPage) o).getKey().equals(getKey());
    }

    @Override
    public String toString() {
        return getKey();
    }
}
