package com.mvzic.extra.page;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.FileListLoadedEvent;
import com.mvzic.extra.event.FileSelectedEvent;
import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.menu.AppDropboxReloadEvent;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.DotDotDotEntry;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.FileTableView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.List;

/**
 * The file page controller.
 *
 * @since 1.0.0
 */
public final class FilePage extends AppPage {
    private ObservableList<Entry> files;
    private final FileTableView table;
    private final DropboxHandler dropbox;

    public FilePage(final WatchedEventBus eventBus, final UnicodeBundle lang, final DropboxHandler dropbox) {
        super(eventBus, lang);

        this.dropbox = dropbox;
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

        reloadDropboxFiles(dropbox.getPath());

//        table.getSelectionModel().selectedItemProperty().addListener(
//                (ObservableValue<? extends Entry> observable, Entry oldValue, Entry newValue) -> {
//                    if (newValue != null) {
//                        //eventBus.post(new FileSelectedEvent(newValue.getPath()));
//                    }
//                });
    }

    @Subscribe
    void listenDropboxReload(final AppDropboxReloadEvent event) {
        reloadDropboxFiles(Path.ROOT);
    }

    private void reloadDropboxFiles(final String path) {
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(() -> {
                        if (path != null) {
                            getEventBus().post(new MessagedEvent("[dropbox] open: " + path));
                        }
                    });

                    String next = path;

                    if (path.equals(Path.PARENT)) {
                        next = dropbox.getPath().substring(0, dropbox.getPath().lastIndexOf('/'));
                    }

                    getEventBus().post(new FileListLoadedEvent(dropbox.getFiles(next)));
                    dropbox.setPath(next);
                } catch (ListFolderErrorException e) {
                    Platform.runLater(() -> getEventBus().post(new MessagedEvent("[dropbox] was not a folder: " + path)));
                } catch (DbxException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).start();
    }

    @Subscribe
    public void listenFileSelect(final FileSelectedEvent event) {
        getEventBus().post(new MessagedEvent("[list] selected: " + event.getFileName()));
        reloadDropboxFiles(event.getFileName());
    }

    @Subscribe
    public void listenFileListChange(final FileListLoadedEvent event) {
        Platform.runLater(() -> {
            setFiles(event.getFiles());
            getEventBus().post(new MessagedEvent("[dropbox] files have been loaded"));
        });
    }

    @Override
    public String getKey() {
        return "FILES";
    }

    /**
     * Sets the table data values.
     *
     * @param entries The data to set with.
     * @since 1.0.0
     */
    private void setFiles(final List<Entry> entries) {
        if (entries.isEmpty()) {
            return;
        }

        // The magic dotdotdot entry
        if (!dropbox.getPath().equals(Path.ROOT)) {
            entries.add(0, new DotDotDotEntry());
        }

        files.setAll(entries);
        table.sort();
    }
}
