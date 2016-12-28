package com.mvzic.extra.ui;

import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;

public class FileTableView extends TableView<Entry> {
    private static final double colWidth = 40.0;

    public FileTableView(final UnicodeBundle lang) {
        super();

        TableColumn folderCol = new TableColumn(lang.get("table_col_folder"));
        folderCol.setMaxWidth(colWidth);
        folderCol.setMinWidth(colWidth);
        folderCol.setResizable(false);
        folderCol.setCellValueFactory(new PropertyValueFactory<>("folder"));

        TableColumn nameCol = new TableColumn(lang.get("table_col_name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn sizeCol = new TableColumn(lang.get("table_col_size"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setMaxWidth(colWidth);
        sizeCol.setMinWidth(colWidth);
        sizeCol.setResizable(false);

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        getColumns().addAll(folderCol, nameCol, sizeCol);

        // Remove blue border
        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-padding: 1em;");

        initSort();
    }

    /**
     * Sorts the filename column with root path item at the top.
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
