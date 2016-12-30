package com.mvzic.extra.model;

/**
 * A track class.
 *
 * @version 1
 * @since 1.0.0
 */
public final class Track {
    private final String artist;
    private final String tile;
    private final String path;

    public Track(String artist, String tile, String path) {
        this.artist = artist;
        this.tile = tile;
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public String getTile() {
        return tile;
    }

    public String getPath() {
        return path;
    }
}
