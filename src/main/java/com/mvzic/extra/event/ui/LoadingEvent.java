package com.mvzic.extra.event.ui;

public final class LoadingEvent {
    private final boolean state;

    public LoadingEvent(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
