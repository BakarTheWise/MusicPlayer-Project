package com.home.demo;

import java.util.ArrayList;
import java.util.List;

public class  Playlist {
    private String name;
    private int numberOfSongs;
    private SongData songData;
    private List<SongData> songs;

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public int getNumberOfSongs() {
        return getNumberOfSongs();
    }
    public List<SongData> getSongs() {
        return songs;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
    public void addSong(SongData songData) {
        songs.add(songData);
    }
    public void removeSong(SongData songData) {
        songs.remove(songData);
    }
}
