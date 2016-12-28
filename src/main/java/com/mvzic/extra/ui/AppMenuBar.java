package com.mvzic.extra.ui;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.menu.AppDropboxReloadEvent;
import com.mvzic.extra.event.menu.AppSwitchedToSettingsEvent;
import com.mvzic.extra.event.menu.AppTerminatedEvent;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.page.SettingsPage;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * The application menu bar component.
 *
 * @since 1.0.0
 */
public final class AppMenuBar extends MenuBar {
    /**
     * Constructs a new menu bar.
     *
     * @param eventBus The event bus reference to pass events outside.
     * @param lang     The language bundle reference to set language dependent values.
     * @since 1.0.0
     */
    public AppMenuBar(final EventBus eventBus, final UnicodeBundle lang) {
        Menu menu = new Menu(lang.get("menu_file"));

        MenuItem options = new MenuItem(lang.get("menu_options"));
        options.setOnAction(e -> eventBus.post(new AppSwitchedToSettingsEvent()));

        MenuItem reload = new MenuItem(lang.get("menu_reload"));
        reload.setOnAction(event -> eventBus.post(new AppDropboxReloadEvent()));

        MenuItem quit = new MenuItem(lang.get("menu_exit"));
        quit.setOnAction(e -> eventBus.post(new AppTerminatedEvent()));
        menu.getItems().add(options);
        menu.getItems().add(reload);
        menu.getItems().add(quit);

        this.getMenus().add(menu);
    }
}
