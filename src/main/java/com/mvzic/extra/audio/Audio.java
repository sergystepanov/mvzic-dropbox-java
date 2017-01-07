package com.mvzic.extra.audio;

import com.mvzic.extra.file.FileItem;

public class Audio extends FileItem {
    public static boolean isAllowedExtension(final String fileName) {
        final String ex = getExtension(fileName).toLowerCase();

        return ex.endsWith("mp3") || ex.endsWith("flac");
    }
}
