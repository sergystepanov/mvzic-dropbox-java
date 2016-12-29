package com.mvzic.extra.event;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.page.AppPage;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom event bus implementation with watched events tracking.
 *
 * @since 1.0.0
 */
public class WatchedEventBus extends EventBus {
    private final static Logger LOGGER = Logger.getLogger(WatchedEventBus.class.getName());
    private Set<AppPage> watched;

    public WatchedEventBus(final String identifier) {
        super(identifier);

        this.watched = new HashSet<>();
    }

    /**
     * Adds a page to a watch list.
     *
     * @param page A single page object to track events from.
     * @since 1.0.0
     */
    public void registerSingleWatched(final AppPage page) {
        unregisterAll();
        register(page);
        watched.add(page);

        LOGGER.log(Level.INFO, "watching {0}, total {1}", new Object[]{page.toString(), watched.size()});
    }

    /**
     * Removes all events form the watching list.
     *
     * @since 1.0.0
     */
    private void unregisterAll() {
        for (final Object sub : watched) {
            unregister(sub);
        }

        watched.clear();
    }
}
