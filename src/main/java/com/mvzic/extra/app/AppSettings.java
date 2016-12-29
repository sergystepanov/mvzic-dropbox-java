package com.mvzic.extra.app;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public final class AppSettings {
    private static final String settingsRootName = "mvzic-dropbox-java";
    private final Preferences preferences;

    public AppSettings() {
        preferences = Preferences.userRoot().node(settingsRootName);

        try {
            String[] keys = preferences.keys();
            for (String key : keys) {
                System.out.println(key);
                System.out.println(preferences.get(key, ""));
            }
        } catch (BackingStoreException e) {
            System.out.println(e.getMessage());
        }
    }

    public void set(final Settings key, final String value) {
        System.out.println(key.name() + " -<");
        preferences.put(key.name(), value);
    }

    public String get(final Settings key) {
        return preferences.get(key.name(), "");
    }
}
