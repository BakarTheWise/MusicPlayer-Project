package com.home.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class SongsLibrary {
    private SongData song;
    private File musicDirectory;
    private File[] musicFiles;
    private List<SongData> allSongsData = new ArrayList<>();
    public List<File> allSongs;
    private List<SongData> songs = new ArrayList<>();

    public void addSong(SongData song) {
        songs.add(song);
    }
    public List<SongData> getSongs() {
        return songs;
    }

}