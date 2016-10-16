package com.mvzic.extra.event;

public class FileSelectedEvent {
    private final String fileName;

    public FileSelectedEvent(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
