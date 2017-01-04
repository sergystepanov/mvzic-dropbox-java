package com.mvzic.extra.audio;

public class Audio {
    public static boolean isAllowedExtension(final String fileName) {
        final String ex = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

        return ex.endsWith(".mp3") || ex.endsWith(".flac");
    }
}
