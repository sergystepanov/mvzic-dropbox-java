package com.mvzic.extra.property;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry {
    private final StringProperty name;
    private final StringProperty path;
    private final BooleanProperty folder;

    public Entry(String name, String path, boolean folder) {
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(path);
        this.folder = new SimpleBooleanProperty(folder);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isFolder() {
        return folder.get();
    }

    public BooleanProperty folderProperty() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder.set(folder);
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }
}
