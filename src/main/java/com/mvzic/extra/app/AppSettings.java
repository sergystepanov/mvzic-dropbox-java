package com.mvzic.extra.app;

import lombok.extern.slf4j.Slf4j;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The application settings class.
 *
 * @since 1.0.0
 */
@Slf4j
public final class AppSettings {
    private static final String SETTINGS_ROOT_NAME = "mvzic-dropbox-java";
    private final Preferences preferences;

    public AppSettings() {
        preferences = Preferences.userRoot().node(SETTINGS_ROOT_NAME);
        dump();
    }

    public void set(final Settings key, final String value) {
        preferences.put(key.name(), value);
        log.info("[settings] set param: {} with: {}", key.name(), value);
    }

    public String get(final Settings key) {
        return preferences.get(key.name(), "");
    }

    /**
     * Prints all settings into log.
     *
     * @since 1.0.0
     */
    private void dump() {
        try {
            for (String key : preferences.keys()) {
                log.info("[settings] key: {}, value: {}", key, preferences.get(key, ""));
            }
        } catch (BackingStoreException e) {
            log.error("[settings] couldn't read settings: ", e.getMessage());
        }
    }
}
