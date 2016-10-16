package com.mvzic.extra.page;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.FileSelectedEvent;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;

public class FilePage extends Pane {
    private final ObservableList<String> files;

    public FilePage(final EventBus eventBus) {
        ListView<String> list = new ListView<>();
        files = FXCollections.observableArrayList();
        list.setItems(files);

        list.prefWidthProperty().bind(this.widthProperty());
        list.prefHeightProperty().bind(this.heightProperty());

        this.getChildren().add(list);

        list.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    eventBus.post(new FileSelectedEvent(new_val));
                });
    }

    public void setFiles(final List<String> files) {
        this.files.clear();
        this.files.addAll(files);
    }
}
