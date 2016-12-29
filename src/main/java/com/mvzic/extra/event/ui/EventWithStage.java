package com.mvzic.extra.event.ui;

import javafx.stage.Stage;

public interface EventWithStage {
    Stage getStage();

    EventWithStage setStage(final Stage stage);
}
