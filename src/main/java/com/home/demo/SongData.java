package com.home.demo;

public class SongData {
    private int index;
    private byte[] albumArtBytes;
    private String title;
    private String artist;
    private String album;
    private String duration;
    private boolean liked = false;
    private String filePath;
    private double durationInSeconds;

    public SongData() {
    }

    public byte[] getAlbumArtBytes() {
        return albumArtBytes;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setAlbumArtBytes(byte[] albumArtBytes) {
        this.albumArtBytes = albumArtBytes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDuration(String duration, double durationInSeconds) {
        this.duration = duration;
        this.durationInSeconds = durationInSeconds;
    }

    public double getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
