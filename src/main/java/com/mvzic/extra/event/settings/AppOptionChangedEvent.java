package com.mvzic.extra.event.settings;

import com.mvzic.extra.app.Settings;

public final class AppOptionChangedEvent {
    private final Settings optionName;
    private final String value;

    public AppOptionChangedEvent(Settings optionName, String value) {
        this.optionName = optionName;
        this.value = value;
    }

    public Settings getOption() {
        return optionName;
    }

    public String getValue() {
        return value;
    }
}
