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
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class Main extends Application {
    private static DropboxHandler dropbox;
    private AppPage currentPage;
    private static final WatchedEventBus eventBus = new WatchedEventBus();
    private static final UnicodeBundle lang = new UnicodeBundle(ResourceBundle.getBundle("i18l.MyBundle"));
    private static final AppSettings appSettings = new AppSettings();
    private BorderPane root;
    private Stage primaryStage;
    private final static long start = System.nanoTime();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        dropbox = new DropboxHandler(appSettings.get(Settings.TOKEN));

        final Bar bar = new Bar();
        root = new BorderPane(null, new AppMenuBar(eventBus, lang), null, bar, null);

        eventBus.register(this);
        eventBus.register(bar);

        resizeStage();
        showStartPage();

        primaryStage.show();

        log.info("[app] start time: {} ms", (System.nanoTime() - start) / 1000000);
    }

    @Subscribe
    void onSettingsChanged(final AppOptionChangedEvent event) {
        if (event.getOption().equals(Settings.TOKEN)) {
            dropbox = new DropboxHandler(event.getValue());
            eventBus.post(new MessagedEvent("[dropbox] has been reloaded"));
        }
    }

    @Subscribe
    void onStartPageSelect(final StartPageSelectedEvent event) {
        showStartPage();
    }

    @Subscribe
    void onSettingsPageSelect(final AppSwitchedToSettingsEvent event) {
        switchTo(new SettingsPage(eventBus, lang, appSettings));
    }

    @Subscribe
    void onDialogSelect(final PopupWindowEvent event) {
        eventBus.post(event.getCallback().setStage(primaryStage));
    }

    @Subscribe
    void onExit(final AppTerminatedEvent event) {
        Platform.exit();
    }

    /**
     * Changes current page to the provided one.
     *
     * @param page the {@code page} to switch to.
     * @since 1.0.0
     */
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

    /**
     * Shows current starting page of the app.
     *
     * @since 1.0.0
     */
    private void showStartPage() {
        switchTo(new FilePage(eventBus, lang, dropbox, appSettings));
    }

    /**
     * Resize the stage to fit it into the screen resolution.
     *
     * @since 1.0.0
     */
    private void resizeStage() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setScene(new Scene(root, screenBounds.getWidth() / 2, screenBounds.getHeight() / 1.2));
    }

    public static void main(String... args) {
        launch(args);
    }
}
