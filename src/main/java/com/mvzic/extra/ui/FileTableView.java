package com.mvzic.extra.ui;

import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Comparator;

public class FileTableView extends TableView<Entry> {
    private static final double colWidth = 40.0;

    public FileTableView(final UnicodeBundle lang) {
        super();

        TableColumn<Entry, String> folderCol = new TableColumn<>(lang.get("table_col_folder"));
        folderCol.setMaxWidth(colWidth);
        folderCol.setMinWidth(colWidth);
        folderCol.setResizable(false);
        folderCol.setCellValueFactory(new PropertyValueFactory<>("folder"));

        TableColumn<Entry, String> nameCol = new TableColumn<>(lang.get("table_col_name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(item -> new TableCell<Entry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    final HBox container = new HBox();

                    if (item.endsWith(".flac")) {
                        final Label tag = new Label();
                        tag.setText("[FLAC] ");
                        container.getChildren().add(tag);
                        //setStyle("-fx-background-color: yellow");
                    }
                    container.getChildren().add(new Label(item));

                    setGraphic(container);
                }
            }
        });

        TableColumn<Entry, Integer> sizeCol = new TableColumn<>(lang.get("table_col_size"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setMaxWidth(colWidth);
        sizeCol.setMinWidth(colWidth);
        sizeCol.setResizable(false);

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        getColumns().add(folderCol);
        getColumns().add(nameCol);
        getColumns().add(sizeCol);

        // Remove blue border
        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-padding: 1em;");

        initSort();
    }

    /**
     * Sorts the filename column with root path item {@link Path#PARENT} at the top.
     *
     * @since 1.0.0
     */

    private void initSort() {
        this.sortPolicyProperty().set(param -> {
            Comparator<Entry> comparator = (top, bottom) ->
                    top.getName().equals(Path.PARENT) ? -1
                            : bottom.getName().equals(Path.PARENT) ? 1
                            : param.getComparator() == null ? 0
                            : param.getComparator().compare(top, bottom);
            FXCollections.sort(param.getItems(), comparator);

            return true;
        });
    }
}
