package com.example.musicapp;

import java.io.Serializable;

public class Song implements Serializable {
    String name;
    String artist;
    String album;
    String track;
    String art;

    public Song(String artist, String name, String album, String track, String art) {
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.track = track;
        this.art = art;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

}
