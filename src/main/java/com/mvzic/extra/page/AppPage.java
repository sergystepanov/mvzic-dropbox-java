package com.mvzic.extra.page;

import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.lang.UnicodeBundle;
import javafx.scene.layout.Pane;

/**
 * Default application page.
 *
 * @since 1.0.0
 */
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

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && ((AppPage) o).getKey().equals(getKey());
    }
}
