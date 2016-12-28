package com.mvzic.extra;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.FileListLoadedEvent;
import com.mvzic.extra.event.FileSelectedEvent;
import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.StartPageSelectedEvent;
import com.mvzic.extra.event.settings.TokenSetEvent;
import com.mvzic.extra.event.menu.AppDropboxReloadEvent;
import com.mvzic.extra.event.menu.AppSwitchedToSettingsEvent;
import com.mvzic.extra.event.menu.AppTerminatedEvent;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.page.FilePage;
import com.mvzic.extra.page.SettingsPage;
import com.mvzic.extra.property.DotDotDotEntry;
import com.mvzic.extra.property.Entry;
import com.mvzic.extra.ui.AppMenuBar;
import com.mvzic.extra.ui.Bar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class Main extends Application {
    private DropboxHandler dropbox;
    private Pane mainPane;
    private static final EventBus eventBus = new EventBus(Settings.MAIN_BUS);
    private static final UnicodeBundle lang = new UnicodeBundle(ResourceBundle.getBundle("i18l.MyBundle"));
    private String rout;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        rout = "";

        AppSettings appSettings = new AppSettings();
        String token = appSettings.get(Settings.TOKEN);
        dropbox = new DropboxHandler(token);

        root = new BorderPane();
        root.setTop(new AppMenuBar(eventBus, lang));

        // Resize to fit into viewport
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth() / 2, screenBounds.getHeight() / 1.2);
        primaryStage.setScene(scene);

        Bar bar = new Bar();
        root.setBottom(bar);

        eventBus.register(this);
        eventBus.register(bar);

        showStartPage();

        primaryStage.show();
    }

    @Subscribe
    public void listenFileListChange(final FileListLoadedEvent event) {
        Platform.runLater(() -> {
            setFiles(event.getFiles());
            eventBus.post(new MessagedEvent("[dropbox] files have been loaded"));
        });
    }

    @Subscribe
    public void listenFileSelect(final FileSelectedEvent event) {
        eventBus.post(new MessagedEvent("[list] selected: " + event.getFileName()));
        reloadDropboxFiles(event.getFileName());
    }

    @Subscribe
    public void listenTokenSet(final TokenSetEvent event) {
        new AppSettings().set(Settings.TOKEN, event.getToken());
        dropbox = new DropboxHandler(event.getToken());
        eventBus.post(new MessagedEvent("[settings] the token has been set"));
    }

    @Subscribe
    void listenStartPageSelect(final StartPageSelectedEvent event) {
        showStartPage();
    }

    // Menu event listeners
    @Subscribe
    void listenMenuSettingsPage(final AppSwitchedToSettingsEvent event) {
        switchTo(new SettingsPage(eventBus));
    }

    @Subscribe
    void listenDropboxReload(final AppDropboxReloadEvent event) {
        reloadDropboxFiles(Path.ROOT);
    }

    @Subscribe
    void listenMenuExit(final AppTerminatedEvent event) {
        Platform.exit();
    }

    private void switchTo(final Pane pane) {
        mainPane = pane;

        // Set the center element contents
        pane.prefWidthProperty().bind(root.widthProperty());
        pane.prefHeightProperty().bind(root.heightProperty());
        root.setCenter(pane);
    }

    private void showStartPage() {
        switchTo(new FilePage(eventBus, lang));
        reloadDropboxFiles(Path.ROOT);
    }

    private void setFiles(final List<Entry> files) {
        if (files.isEmpty()) {
            return;
        }

        // The magic dotdotdot entry
        if (!rout.isEmpty()) {
            files.add(0, new DotDotDotEntry());
        }

        ((FilePage) mainPane).setFiles(files);
    }

    private void reloadDropboxFiles(final String path) {
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(() -> {
                        if (path != null) {
                            eventBus.post(new MessagedEvent("[dropbox] open: " + path));
                        }
                    });

                    String next = path;

                    if (path.equals(Path.PARENT)) {
                        next = rout.substring(0, rout.lastIndexOf('/'));
                    }

                    eventBus.post(new FileListLoadedEvent(dropbox.getFiles(next)));
                    rout = next;
                } catch (ListFolderErrorException e) {
                    Platform.runLater(() -> eventBus.post(new MessagedEvent("[dropbox] was not a folder: " + path)));
                } catch (DbxException | IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
