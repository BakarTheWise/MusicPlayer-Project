package com.home.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SongController implements Initializable {
    @FXML
    private Button songButton;
    @FXML
    private GridPane songPane;
    @FXML
    private ToggleButton likeButton;
    @FXML
    private ImageView heartImage;
    @FXML
    private ImageView albumArtImageView;
    @FXML
    private Label totalSongsLabel;
    @FXML
    private Label numLabel;
    @FXML
    private Label songLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label albumLabel;
    @FXML
    private Label durationLabel;
    private static Media media;
    private static MediaPlayer mediaPlayer;
    private SongData songData;
    private static List<SongData> likedSongs;
    private Playlist likedSongsPlaylist;
    private static Playlist currentPlaylist = MainController.getCurrentPlaylist();
    private boolean playing = false;
    private static int currentSOngIndex;
    private static String filePath;
    private Preferences preferences;
    private static boolean liked = false;
    Image unFilledHeartImage = new Image(getClass().getResourceAsStream("unlike.png"));
    Image filledHeartImage = new Image(getClass().getResourceAsStream("liked.png"));


    public void setNumLabel(int index) {
        numLabel.setText(String.valueOf(index));
    }
    public void setAlbumArtImageView(byte[] albumArtBytes) {
        albumArtImageView.setImage(new Image(new ByteArrayInputStream(albumArtBytes)));
    }
    public void setSongLabel(String title) {
        songLabel.setText(title);
    }
    public void setArtistLabel(String artist) {
        artistLabel.setText(artist);
    }
    public void setAlbumLabel(String album) {
        albumLabel.setText(album);
    }
    public void setDurationLabel(String duration) {
        durationLabel.setText(duration);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        likedSongs = new ArrayList<>();

    }


    public void setSongData(SongData songData) {
        this.songData = songData;
    }
    @FXML
    public void playSongOnClick() {
        songPane.setStyle("-fx-background-color: #2A2A2A;");
        playing = true;
        currentSOngIndex =  currentPlaylist.getSongs().indexOf(songData);
        filePath = currentPlaylist.getSongs().get(currentSOngIndex).getFilePath();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        playMedia();
    }
    public static void playMedia() {
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose();
            nextMedia();
        });
        mediaPlayer.play();
    }

    public static void nextMedia() {
        if (currentSOngIndex < currentPlaylist.getSongs().size() - 1) {
            currentSOngIndex++;
        } else {
            currentSOngIndex = 0;
        }
        filePath = currentPlaylist.getSongs().get(currentSOngIndex).getFilePath();
        mediaPlayer.stop();
        mediaPlayer.dispose();

        media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        playMedia();
    }

    public void setLiked(boolean liked) {
        SongController.liked = liked;
    }
    public void toggleButtonAction(ActionEvent event) {
        if (!liked) {
            likeButton.setGraphic(new ImageView(filledHeartImage));
            liked = true;
            likedSongs.add(songData);

        } else {
            likeButton.setGraphic(new ImageView(unFilledHeartImage));
            liked = false;
            likedSongs.remove(songData);
        }
    }
    @FXML
    public void mouseEntered() {
        songPane.setStyle("-fx-background-color: #2A2A2A;");
    }
    @FXML
    public void mouseExited() {
        songPane.setStyle("-fx-background-color: #0F111A;");
    }
    public static List<SongData> getLikedSongs() {
        return likedSongs;
    }
}