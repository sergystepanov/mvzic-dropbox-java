package com.mvzic.extra.event;

import com.mvzic.extra.property.Entry;

import java.util.List;

public class FileListLoadedEvent {
    private final List<Entry> files;

    public FileListLoadedEvent(List<Entry> files) {
        this.files = files;
    }

    public List<Entry> getFiles() {
        return files;
    }
}
