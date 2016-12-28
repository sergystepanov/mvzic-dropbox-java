package com.mvzic.extra.event.settings;

public class TokenSetEvent {
    private final String token;

    public TokenSetEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}