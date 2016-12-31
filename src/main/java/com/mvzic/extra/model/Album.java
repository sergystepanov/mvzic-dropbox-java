package com.mvzic.extra.model;

import java.util.Collections;
import java.util.List;

/**
 * An album class.
 *
 * @version 1
 * @since 1.0.0
 */
public final class Album {
    private final String id;
    private final String date;
    private final String name;
    private final String link;
    private final List<Track> tracks;

    public Album(String id, String date, String name, String link, List<Track> tracks) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.link = link;
        this.tracks = Collections.unmodifiableList(tracks);
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
