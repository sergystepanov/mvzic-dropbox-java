package com.mvzic.extra.lang;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class UnicodeBundle {
    private final ResourceBundle resourceBundle;

    public UnicodeBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String get(final String key) {
        return getUtf(resourceBundle.getString(key));
    }

    private static String getUtf(final String name) {
        try {
            return new String(name.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
