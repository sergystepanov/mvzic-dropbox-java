package com.mvzic.extra.ui;

import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        getColumns().addAll(folderCol, nameCol);

        // Remove blue border
        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-padding: 10px;");
    }
}
