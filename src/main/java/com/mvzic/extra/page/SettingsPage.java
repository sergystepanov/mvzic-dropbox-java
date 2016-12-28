package com.mvzic.extra.page;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.StartPageSelectedEvent;
import com.mvzic.extra.event.settings.LocalPathSetEvent;
import com.mvzic.extra.event.settings.TokenSetEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class SettingsPage extends Pane {
    public SettingsPage(final EventBus eventBus) {
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

        final Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> eventBus.post(new StartPageSelectedEvent()));
        GridPane.setConstraints(exit, 0, 3);
        grid.getChildren().add(exit);

        getChildren().add(grid);
    }
}
