package com.mvzic.extra.ui;

import com.mvzic.extra.audio.Audio;
import com.mvzic.extra.file.FileItem;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.Entry;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.Comparator;

public class FileTableView extends TableView<Entry> {
    public FileTableView(final UnicodeBundle lang) {
        super();

        TableColumn<Entry, String> nameCol = new TableColumn<>(lang.get("table_col_name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(100);

        nameCol.setCellFactory(items -> new TableCell<Entry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    final HBox container = new HBox();
                    container.setSpacing(5);
                    container.setAlignment(Pos.BASELINE_LEFT);

                    if (Audio.isAllowedExtension(item)) {
                        Entry e = (Entry) this.getTableRow().getItem();

                        container.getChildren().add(new FileTag(FileItem.getExtension(item).toUpperCase()));
                        if (null != e) {
                            container.getChildren().add(new FileTag(String.valueOf(e.getSize())));
                        }
                    }
                    container.getChildren().add(new Label(item));

                    setGraphic(container);
                }
            }
        });

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        getColumns().add(nameCol);

        // Remove blue border
        setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-padding: .5em;");

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
