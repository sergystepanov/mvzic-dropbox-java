package com.mvzic.extra.file;

public class FileItem {
    public static String getExtension(final String name) {
        final int index = name.lastIndexOf('.');

        return index > 0 ? name.substring(index + 1) : "";
    }
}
