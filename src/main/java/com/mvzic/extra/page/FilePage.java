package com.mvzic.extra.page;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.audio.Audio;
import com.mvzic.extra.audio.AudioMetadataReader;
import com.mvzic.extra.audio.JaudiotagReader;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.ExplorePathEvent;
import com.mvzic.extra.event.FolderOpenedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.menu.AppDropboxReloadEvent;
import com.mvzic.extra.event.ui.LoadingEvent;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.property.DotDotDotEntry;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.FileTableView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The file page controller.
 *
 * @since 1.0.0
 */
@Slf4j
public final class FilePage extends AppPage {
    private final ObservableList<Entry> files;
    private final FileTableView table;
    private final DropboxHandler dropbox;
    private final AppSettings settings;

    public FilePage(WatchedEventBus eventBus, UnicodeBundle lang, DropboxHandler dropbox, AppSettings settings) {
        super(eventBus, lang);

        files = FXCollections.observableArrayList();
        this.dropbox = dropbox;
        this.settings = settings;

        table = new FileTableView(lang);
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

        // Setup a menu
        table.setRowFactory(param -> {
            final TableRow<Entry> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem explore = new MenuItem(lang.get("table_menu_explore"));
            explore.setOnAction(event -> eventBus.post(new ExplorePathEvent(row.getItem().getPath())));
            contextMenu.getItems().add(explore);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu)
            );

            return row;
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

    @Subscribe
    void onPathExplored(final ExplorePathEvent event) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        try {
            java.nio.file.Path path = Paths.get(settings.get(Settings.LOCAL_PATH), event.getPath());
            Desktop.getDesktop().open(path.toFile().isFile() ? path.getParent().toFile() : path.toFile());
        } catch (IOException e) {
            log.error("[list] an error with {}, {}", event.getPath(), e.getMessage());
        }
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
                getEventBus().post(new LoadingEvent(true));
                message("[dropbox] to open: " + path);
                showFiles(dropbox.getFiles(path));
                message("[dropbox] files have been loaded");
            } catch (ListFolderErrorException e) {
                message("[dropbox] was not a folder: " + path);
            } catch (DbxException e) {
                message("[dropbox] an api exception, " + e.getMessage());
            } finally {
                getEventBus().post(new LoadingEvent(false));
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
                final File file = Paths.get(settings.get(Settings.LOCAL_PATH), entry.getPath()).toFile();
                if (file.exists() && Audio.isAllowedExtension(file.getName())) {
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
