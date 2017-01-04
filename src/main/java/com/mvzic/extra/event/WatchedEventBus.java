package com.mvzic.extra.event;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.page.AppPage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * A custom event bus implementation with watched events tracking.
 *
 * @since 1.0.0
 */
@Slf4j
public class WatchedEventBus extends EventBus {
    private final Set<AppPage> watched;

    public WatchedEventBus() {
        super();

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

        log.info("watching {}, total {}", page.toString(), watched.size());
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
