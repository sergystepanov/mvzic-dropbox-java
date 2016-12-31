package com.mvzic.extra.page;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.audio.AudioMetadataReader;
import com.mvzic.extra.audio.JaudiotagReader;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.FolderOpenedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.menu.AppDropboxReloadEvent;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.DotDotDotEntry;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.FileTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;
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
    private final AppSettings settings;

    public FilePage(final WatchedEventBus eventBus, final UnicodeBundle lang, DropboxHandler dropbox,
                    AppSettings settings) {
        super(eventBus, lang);

        this.dropbox = dropbox;
        this.settings = settings;

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
                    eventBus.post(new FolderOpenedEvent(anEntry.getPath()));
                }
            }
        });

        listDropboxFolder(dropbox.getPath());
    }

    @Subscribe
    void onDropboxReset(final AppDropboxReloadEvent event) {
        listDropboxFolder(Path.ROOT);
    }

    @Subscribe
    void onFolderOpened(final FolderOpenedEvent event) {
        listDropboxFolder(event.getPath());
        message("[list] selected: " + event.getPath());
    }

    @Override
    public String getKey() {
        return "FILES";
    }

    /**
     * Loads files from the provided {@code path}.
     *
     * @param path A path to load files from.
     * @since 1.0.0
     */
    private void listDropboxFolder(final String path) {
        new Thread(() -> {
            try {
                message("[dropbox] to open: " + path);
                showFiles(dropbox.getFiles(path));
                message("[dropbox] files have been loaded");
            } catch (ListFolderErrorException e) {
                message("[dropbox] was not a folder: " + path);
            } catch (DbxException e) {
                message("[dropbox] an api exception, " + e.getMessage());
            }
        }).start();
    }

    /**
     * Sets the table data values.
     *
     * @param entries The data to set with.
     * @since 1.0.0
     */
    private void showFiles(final List<Entry> entries) {
        if (entries.isEmpty()) {
            return;
        }

        List<Entry> list = new ArrayList<>(readLocalData(entries));

        // The magic dotdotdot entry
        if (!dropbox.getPath().equals(Path.ROOT)) {
            list.add(0, new DotDotDotEntry());
        }

        files.setAll(list);
        table.sort();
    }

    /**
     * Reads local data of the Dropbox files.
     *
     * @param entries The entry to read data for.
     * @return A list of the entries with its data.
     * @since 1.0.0
     */
    private List<Entry> readLocalData(final List<Entry> entries) {
        final String localPath = settings.get(Settings.LOCAL_PATH);

        if (localPath.isEmpty()) {
            return entries;
        }

        List<Entry> updated = new ArrayList<>();
        for (final Entry entry : entries) {
            // a file
            if (!entry.folderProperty().get()) {
                final File file = new File(settings.get(Settings.LOCAL_PATH) + entry.getPath());
                if (file.exists()) {
                    AudioMetadataReader reader = new JaudiotagReader();
                    entry.setSize((int) file.length() / 1024 / 1024);
                    reader.getTag(file);
                }
            }

            updated.add(entry);
        }

        return updated;
    }
}
