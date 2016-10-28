package com.mvzic.extra.page;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.FileSelectedEvent;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.FileTableView;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FilePage extends Pane {
    private final ObservableList<Entry> files;

    public FilePage(final EventBus eventBus, final UnicodeBundle lang) {

        FileTableView table = new FileTableView(lang);

        // Fit to parent container
        table.prefWidthProperty().bind(this.widthProperty());
        table.prefHeightProperty().bind(this.heightProperty());

        files = FXCollections.observableArrayList();
        table.setItems(files);

        this.getChildren().add(table);
//
//        table.getSelectionModel().selectedItemProperty().addListener(
//                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
//                    eventBus.post(new FileSelectedEvent(new_val));
//                });
    }

    public void setFiles(final List<Entry> files) {
        this.files.clear();
        this.files.addAll(files);
    }
}
