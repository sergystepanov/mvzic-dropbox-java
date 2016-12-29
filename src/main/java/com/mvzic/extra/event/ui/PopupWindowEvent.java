package com.mvzic.extra.event.ui;

public class PopupWindowEvent {
    private final EventWithStage callback;

    public PopupWindowEvent(EventWithStage callback) {
        this.callback = callback;
    }

    public EventWithStage getCallback() {
        return callback;
    }
}
