package com.mvzic.extra.page;

import com.google.common.eventbus.Subscribe;
import com.mvzic.extra.app.AppSettings;
import com.mvzic.extra.event.StartPageSelectedEvent;
import com.mvzic.extra.event.WatchedEventBus;
import com.mvzic.extra.event.settings.LocalPathSetEvent;
import com.mvzic.extra.event.settings.TokenSetEvent;
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
    public SettingsPage(final WatchedEventBus eventBus, final UnicodeBundle lang, final AppSettings settings) {
        super(eventBus, lang);

        final GridPane grid = new GridPane();

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        final Label caption = new Label("SETTINGS");
        GridPane.setConstraints(caption, 0, 0);
        grid.getChildren().add(caption);

        final TextField token = new TextField();
        token.setPromptText("Enter your token.");
        GridPane.setConstraints(token, 0, 1);
        grid.getChildren().add(token);

        Button submit = new Button("Set");
        GridPane.setConstraints(submit, 1, 1);
        grid.getChildren().add(submit);
        submit.setOnAction((ActionEvent e) -> eventBus.post(new TokenSetEvent(token.getText())));

        final TextField dropboxPath = new TextField();
        dropboxPath.setPromptText("Enter your Dropbox local path.");
        GridPane.setConstraints(dropboxPath, 0, 2);
        grid.getChildren().add(dropboxPath);

        Button submit2 = new Button("Set");
        GridPane.setConstraints(submit2, 1, 2);
        grid.getChildren().add(submit2);
        submit2.setOnAction((ActionEvent e) -> eventBus.post(new LocalPathSetEvent(dropboxPath.getText())));

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
    void listenDirChange(final DirectorySelectionChangedEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(event.getStage());

        if (dir != null) {
            System.out.println(dir.getAbsolutePath());
        }
    }

    @Override
    public String getKey() {
        return "SETTINGS";
    }
}
