package com.mvzic.extra;

import com.dropbox.core.DbxException;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.FileListLoadedEvent;
import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.TokenSetEvent;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.page.FilePage;
import com.mvzic.extra.page.SettingsPage;
import com.mvzic.extra.ui.Bar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class Main extends Application {
    private DropboxHandler dropbox;
    private Pane mainPane;
    private static final EventBus eventBus = new EventBus(Settings.MAIN_BUS);

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppSettings appSettings = new AppSettings();
        String token = appSettings.get(Settings.TOKEN);

        BorderPane root = new BorderPane();

        UnicodeBundle lang = new UnicodeBundle(ResourceBundle.getBundle("i18l.MyBundle"));

        dropbox = new DropboxHandler(token);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu(lang.get("menu_file"));

        MenuItem options = new MenuItem(lang.get("menu_options"));
        options.setOnAction(event -> {
            SettingsPage page = new SettingsPage(eventBus);
            setCenter(root, page);
        });

        MenuItem reload = new MenuItem(lang.get("menu_reload"));
        reload.setOnAction(event -> reloadDropboxFiles());

        MenuItem quit = new MenuItem(lang.get("menu_exit"));
        quit.setOnAction(e -> Platform.exit());
        menu.getItems().add(options);
        menu.getItems().add(reload);
        menu.getItems().add(quit);
        menuBar.getMenus().add(menu);
        //menuBar.setUseSystemMenuBar(true);

        root.setTop(menuBar);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);

        mainPane = new FilePage();

        setCenter(root, mainPane);

        Bar bar = new Bar();
        root.setBottom(bar);

        primaryStage.show();

        eventBus.register(this);
        eventBus.register(bar);

        reloadDropboxFiles();
    }

    @Subscribe
    public void listenFileListChange(final FileListLoadedEvent event) {
        Platform.runLater(() -> {
            setFiles(event.getFiles());
            eventBus.post(new MessagedEvent("[dropbox] files have been loaded"));
        });
    }

    @Subscribe
    public void listenTokenSet(final TokenSetEvent event) {
        new AppSettings().set(Settings.TOKEN, event.getToken());
        dropbox = new DropboxHandler(event.getToken());
        eventBus.post(new MessagedEvent("[settings] the token has been set"));
    }

    private void setCenter(final BorderPane root, final Pane pane) {
        pane.prefWidthProperty().bind(root.widthProperty());
        pane.prefHeightProperty().bind(root.heightProperty());
        root.setCenter(pane);
    }

    private void setFiles(final List<String> files) {
        if (files.isEmpty()) {
            return;
        }

        ((FilePage) mainPane).setFiles(files);
    }

    private void reloadDropboxFiles() {
        new Thread(new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(() -> eventBus.post(new MessagedEvent("[dropbox] file request ...")));
                    eventBus.post(new FileListLoadedEvent(dropbox.getFiles()));
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
