package com.mvzic.extra.event;

public class MessagedEvent {
    private final String message;

    public MessagedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
