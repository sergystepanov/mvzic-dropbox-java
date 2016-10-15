package com.mvzic.extra.event;

import java.util.List;

public class FileListLoadedEvent {
    private final List<String> files;

    public FileListLoadedEvent(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
