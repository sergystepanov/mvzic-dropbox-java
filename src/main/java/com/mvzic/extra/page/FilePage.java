package com.mvzic.extra.page;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.FileSelectedEvent;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.FileTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * The file page controller.
 *
 * @since 1.0.0
 */
public final class FilePage extends Pane {
    private final ObservableList<Entry> files;
    private final FileTableView table;

    /**
     * Initializes new {@code Pane} with a table.
     *
     * @param eventBus The event bus reference to pass events outside.
     * @param lang     The language bundle reference to set language dependent values in a {@code View}.
     * @since 1.0.0
     */
    public FilePage(final EventBus eventBus, final UnicodeBundle lang) {
        table = new FileTableView(lang);
        files = FXCollections.observableArrayList();

        getChildren().add(table);

        // Fit into parent container
        table.prefWidthProperty().bind(widthProperty());
        table.prefHeightProperty().bind(heightProperty());

        table.setItems(files);

        table.setOnMousePressed(event -> {
            final boolean isDoubleClicked = event.isPrimaryButtonDown() && event.getClickCount() == 2;

            // Handle folder double click
            if (isDoubleClicked) {
                final Entry anEntry = table.getSelectionModel().getSelectedItem();

                // Fire open events for folders only
                if (anEntry.folderProperty().get()) {
                    eventBus.post(new FileSelectedEvent(anEntry.getPath()));
                }
            }
        });

//        table.getSelectionModel().selectedItemProperty().addListener(
//                (ObservableValue<? extends Entry> observable, Entry oldValue, Entry newValue) -> {
//                    if (newValue != null) {
//                        //eventBus.post(new FileSelectedEvent(newValue.getPath()));
//                    }
//                });
    }

    /**
     * Sets the table data values.
     *
     * @param entries The data to set with.
     * @since 1.0.0
     */
    public void setFiles(final List<Entry> entries) {
        files.setAll(entries);
        table.sort();
    }
}
