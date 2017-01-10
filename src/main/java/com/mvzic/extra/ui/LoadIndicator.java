package com.mvzic.extra.ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The loading indicator UI element.
 *
 * @since 1.0.0
 */
final class LoadIndicator {
    private final Text text;

    private final long delay;
    private static final char[] frames = {'-', '\\', '|', '/'};
    private byte frame;

    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduled = null;

    /**
     * @param scheduler the {@code scheduler} for animation repeat.
     * @param fps       the frames per second value to limit the animation speed.
     * @param duration  the duration in seconds of each frame of the animation.
     * @since 1.0.0
     */
    LoadIndicator(ScheduledExecutorService scheduler, final double fps, final double duration) {
        this.scheduler = scheduler;
        delay = (long) (1000 * duration / fps);

        text = new Text();
        text.sceneProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                stop();
            } else {
                play();
            }
        });
    }

    private void frame() {
        text.setText("[" + frames[frame % frames.length] + "] ( ͡° ͜ʖ ͡°)");
        frame = (byte) (frame == Byte.MAX_VALUE ? 0 : frame + 1);
    }

    private void play() {
        stop();
        frame = 0;
        scheduled = scheduler.scheduleAtFixedRate(() ->
                Platform.runLater(this::frame), delay, delay, TimeUnit.MILLISECONDS);
    }

    private void stop() {
        if (scheduled != null) {
            scheduled.cancel(false);
            scheduled = null;
            text.setText("");
        }
    }

    public void set(final boolean state) {
        if (state) {
            play();
        } else {
            stop();
        }
    }

    Node getNode() {
        return text;
    }
}
