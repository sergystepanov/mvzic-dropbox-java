package com.mvzic.extra.audio;

public class AudioTag {
    private final String artist;
    private final String album;

    public AudioTag(String artist, String album) {
        this.artist = artist;
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    @Override
    public String toString() {
        return "AudioTag{" +
                "artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
