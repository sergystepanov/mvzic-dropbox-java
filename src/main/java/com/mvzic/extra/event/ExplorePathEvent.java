package com.mvzic.extra.event;

public final class ExplorePathEvent {
    private final String path;

    public ExplorePathEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
