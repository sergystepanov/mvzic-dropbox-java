package com.mvzic.extra.event.ui;

import javafx.stage.Stage;

public class DirectorySelectionChangedEvent implements EventWithStage {
    private Stage stage;

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public DirectorySelectionChangedEvent setStage(Stage stage) {
        this.stage = stage;

        return this;
    }
}
