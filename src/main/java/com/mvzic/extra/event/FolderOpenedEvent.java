package com.mvzic.extra.event;

/**
 * The event of folder opening.
 *
 * @since 1.0.0
 */
public final class FolderOpenedEvent {
    private final String path;

    public FolderOpenedEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
