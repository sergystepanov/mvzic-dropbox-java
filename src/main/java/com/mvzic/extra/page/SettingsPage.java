package com.mvzic.extra.page;

import com.google.common.eventbus.EventBus;
import com.mvzic.extra.event.TokenSetEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class SettingsPage extends Pane {
    public SettingsPage(final EventBus eventBus) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        final TextField token = new TextField();
        token.setPromptText("Enter your token.");
        GridPane.setConstraints(token, 0, 0);
        grid.getChildren().add(token);

        Button submit = new Button("Set");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        submit.setOnAction((ActionEvent e) -> eventBus.post(new TokenSetEvent(token.getText())));

        this.getChildren().add(new Label("SETTINGS"));
        this.getChildren().add(grid);
    }
}
