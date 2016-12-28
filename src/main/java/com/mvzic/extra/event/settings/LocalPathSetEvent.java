package com.mvzic.extra.event.settings;

public final class LocalPathSetEvent {
    private final String path;

    public LocalPathSetEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
