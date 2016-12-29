package com.mvzic.extra.page;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.app.Settings;
import com.mvzic.extra.event.StartPageSelectedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.settings.AppOptionChangedEvent;
import com.mvzic.extra.event.ui.DirectorySelectionChangedEvent;
import com.mvzic.extra.event.ui.PopupWindowEvent;
import com.mvzic.extra.lang.UnicodeBundle;
import com.mvzic.extra.ui.DirChooserView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class SettingsPage extends AppPage {
    private final AppSettings settings;
    private final TextField dropboxPath;

    public SettingsPage(final WatchedEventBus eventBus, final UnicodeBundle lang, final AppSettings settings) {
        super(eventBus, lang);

        this.settings = settings;
        final GridPane grid = new GridPane();

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        final Label caption = new Label(lang.get("settings_caption"));
        GridPane.setConstraints(caption, 0, 0);
        grid.getChildren().add(caption);

        final TextField token = new TextField();
        token.setPromptText(lang.get("settings_token_placeholder"));
        GridPane.setConstraints(token, 0, 1);
        grid.getChildren().add(token);

        Button submit = new Button(lang.get("settings_set"));
        GridPane.setConstraints(submit, 1, 1);
        grid.getChildren().add(submit);
        submit.setOnAction((ActionEvent e) ->
                eventBus.post(new AppOptionChangedEvent(Settings.TOKEN, token.getText())));

        dropboxPath = new TextField();
        dropboxPath.setPromptText(lang.get("settings_local_path_placeholder"));
        GridPane.setConstraints(dropboxPath, 0, 2);
        grid.getChildren().add(dropboxPath);

        Button submit2 = new Button(lang.get("settings_set"));
        GridPane.setConstraints(submit2, 1, 2);
        grid.getChildren().add(submit2);
        submit2.setOnAction((ActionEvent e) ->
                eventBus.post(new AppOptionChangedEvent(Settings.LOCAL_PATH, dropboxPath.getText())));

        final DirChooserView dir = new DirChooserView(eventBus);
        GridPane.setConstraints(dir, 1, 3);
        dir.setOnAction(e -> eventBus.post(new PopupWindowEvent(new DirectorySelectionChangedEvent())));
        grid.getChildren().add(dir);

        final Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> eventBus.post(new StartPageSelectedEvent()));
        GridPane.setConstraints(exit, 0, 3);
        grid.getChildren().add(exit);

        getChildren().add(grid);
    }

    @Subscribe
    void onSettingsChanged(final AppOptionChangedEvent event) {
        settings.set(event.getOption(), event.getValue());
    }

    @Subscribe
    void listenDirChange(final DirectorySelectionChangedEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(event.getStage());

        if (dir != null) {
            setDirText(dir.getAbsolutePath());
        }
    }

    private void setDirText(final String value) {
        dropboxPath.setText(value);
    }

    @Override
    public String getKey() {
        return "SETTINGS";
    }
}
