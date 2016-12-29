package com.mvzic.extra;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.dropbox.DropboxHandler;
import com.mvzic.extra.event.MessagedEvent;
import com.mvzic.extra.event.StartPageSelectedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.menu.AppSwitchedToSettingsEvent;
import com.mvzic.extra.event.menu.AppTerminatedEvent;
import com.mvzic.extra.event.settings.AppOptionChangedEvent;
import com.mvzic.extra.event.ui.EventWithStage;
import com.mvzic.extra.event.ui.PopupWindowEvent;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.page.AppPage;
import com.mvzic.extra.page.FilePage;
import com.mvzic.extra.page.SettingsPage;
import com.mvzic.extra.ui.AppMenuBar;
import com.mvzic.extra.ui.Bar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {
    private static DropboxHandler dropbox;
    private AppPage currentPage;
    private static final WatchedEventBus eventBus = new WatchedEventBus();
    private static final UnicodeBundle lang = new UnicodeBundle(ResourceBundle.getBundle("i18l.MyBundle"));
    private static final AppSettings appSettings = new AppSettings();
    private BorderPane root;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

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
    void onSettingsChanged(final AppOptionChangedEvent event) {
        if (event.getOption().equals(Settings.TOKEN)) {
            dropbox = new DropboxHandler(event.getValue());
            eventBus.post(new MessagedEvent("[dropbox] has been reloaded"));
        }
    }

    @Subscribe
    void listenStartPageSelect(final StartPageSelectedEvent event) {
        showStartPage();
    }

    // Menu event listeners
    @Subscribe
    void listenMenuSettingsPage(final AppSwitchedToSettingsEvent event) {
        switchTo(new SettingsPage(eventBus, lang, appSettings));
    }

    @Subscribe
    void listenDialogSelected(final PopupWindowEvent event) {
        EventWithStage callback = event.getCallback();
        callback.setStage(primaryStage);

        eventBus.post(callback);
    }

    @Subscribe
    void listenMenuExit(final AppTerminatedEvent event) {
        Platform.exit();
    }

    private void switchTo(final AppPage page) {
        if (currentPage != null && currentPage.equals(page)) {
            return;
        }

        currentPage = page;

        // Set the center element contents
        page.prefWidthProperty().bind(root.widthProperty());
        page.prefHeightProperty().bind(root.heightProperty());
        root.setCenter(page);

        eventBus.registerSingleWatched(page);
    }

    private void showStartPage() {
        switchTo(new FilePage(eventBus, lang, dropbox));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
