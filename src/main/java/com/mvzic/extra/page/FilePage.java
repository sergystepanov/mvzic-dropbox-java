package com.mvzic.extra.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;

public class FilePage extends Pane {
    private final ObservableList<String> files;

    public FilePage() {
        ListView<String> list = new ListView<>();
        files = FXCollections.observableArrayList();
        list.setItems(files);

        list.prefWidthProperty().bind(this.widthProperty());
        list.prefHeightProperty().bind(this.heightProperty());

        this.getChildren().add(list);
    }

    public void setFiles(final List<String> files) {
        this.files.addAll(files);
    }
}
