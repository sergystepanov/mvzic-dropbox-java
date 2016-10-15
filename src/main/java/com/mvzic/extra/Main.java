package com.mvzic.extra;

import com.dropbox.core.DbxException;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.dropbox.DropboxHandler;
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
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppSettings appSettings = new AppSettings();

        String token = appSettings.get(Settings.TOKEN);

        BorderPane root = new BorderPane();

        ResourceBundle lang = ResourceBundle.getBundle("i18l.MyBundle");

        DropboxHandler dropbox = new DropboxHandler(token);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu(getUtf(lang.getString("menu_file")));
        MenuItem options = new MenuItem(getUtf(lang.getString("menu_options")));
        options.setOnAction(event -> {
            SettingsPage page = new SettingsPage();
            setCenter(root, page);
        });
        MenuItem quit = new MenuItem(getUtf(lang.getString("menu_exit")));
        quit.setOnAction(e -> Platform.exit());
        menu.getItems().add(options);
        menu.getItems().add(quit);
        menuBar.getMenus().add(menu);
        //menuBar.setUseSystemMenuBar(true);


        root.setTop(menuBar);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);

        FilePage mainPane = new FilePage();

        setCenter(root, mainPane);

        Bar bar = new Bar();
        root.setBottom(bar);

        primaryStage.show();

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    mainPane.setFiles(dropbox.getFiles());
                } catch (DbxException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        if (!token.isEmpty()) {
            new Thread(task).start();
        }
    }

    private static String getUtf(final String name) {
        try {
            return new String(name.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private void setCenter(final BorderPane root, final Pane pane) {
        pane.prefWidthProperty().bind(root.widthProperty());
        pane.prefHeightProperty().bind(root.heightProperty());
        root.setCenter(pane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
