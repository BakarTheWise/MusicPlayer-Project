package com.home.demo;

import com.jfoenix.controls.JFXSlider;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController implements Initializable {

    @FXML
    private AnchorPane container;

    @FXML
    public VBox contentBox;
    @FXML
    private Button skipButton;
    @FXML
    private Button prevButton;
    @FXML
    private ToggleButton loopButton;
    @FXML
    private ToggleButton shuffleButton;
    @FXML
    private Button pauseOrPlayButton;
    @FXML
    private JFXSlider trackSlider;
    private File musicDirectory;
    private File[] musicFiles;
    private static Playlist currentPlaylist;
    private Node homePageContent;
    private Node songsPageContent;
    private Node libraryPageContent;
    private List<Playlist> playlists = new ArrayList<>();
    private static List<SongData> allSongsData = new ArrayList<>();
    private static List<File> allSongs;
    private Playlist allSongsPlaylist;
    private LibraryController libraryController;
    private PlaylistTableViewController playlistTableViewController;
    private Timer timer;
    private TimerTask timerTask;
    private Color[] colors;


    private int index = 0;
    private boolean playing = false;
    private Parent playlistNode;

    public MainController() {
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        holuplemmecook();

        prevButton.setDisable(true);
        skipButton.setDisable(true);
        pauseOrPlayButton.setDisable(true);
        loopButton.setDisable(true);
        shuffleButton.setDisable(true);

        FXMLLoader playlistLoader = new FXMLLoader(Main.class.getResource("PlaylistTableView.fxml"));
        try {
            playlistLoader.setControllerFactory(param -> new PlaylistTableViewController(this, trackSlider));
            playlistNode = playlistLoader.load();
            playlistTableViewController = playlistLoader.getController();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//
        try {
            FXMLLoader homePageLoader = new FXMLLoader(Main.class.getResource("HomePage.fxml"));
            homePageContent = homePageLoader.load();
            contentBox.getChildren().add(homePageContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        allSongsPlaylist = new Playlist();
        allSongsPlaylist.setName("All Songs");

        musicDirectory = new File("C:\\Users\\Abubakar\\IdeaProjects\\demo\\src\\main\\resources\\com\\home\\demo\\music");
        musicFiles = musicDirectory.listFiles();
        if (musicDirectory.isDirectory()) {
            if (musicFiles != null) {
                for (File musicFile : musicFiles) {
                    try {
                        AudioFile audioFile = AudioFileIO.read(musicFile);
                        Tag tag = audioFile.getTag();

                        String title = tag.getFirst(FieldKey.TITLE);
                        String artist = tag.getFirst(FieldKey.ARTIST);
                        String album = tag.getFirst(FieldKey.ALBUM);
                        String filePath = musicFile.getAbsolutePath();

                        int durationSeconds = audioFile.getAudioHeader().getTrackLength();
                        String duration = durationSeconds / 60 + ":" + durationSeconds % 60;

                        byte[] albumArtBytes = null;
                        Artwork albumArtwork = null;
                        List<Artwork> artworkList = tag.getArtworkList();
                        if (artworkList != null && !artworkList.isEmpty()) {
                            albumArtwork = artworkList.get(0);
                            if (albumArtwork != null) {
                                albumArtBytes = albumArtwork.getBinaryData();
                            }
                        }

                        SongData song = new SongData();

                        song.setIndex(index + 1);
                        song.setAlbumArtBytes(albumArtBytes);
                        song.setTitle(title);
                        song.setArtist(artist);
                        song.setAlbum(album);
                        song.setFilePath(filePath);
                        song.setDuration(duration, durationSeconds);

                        allSongsPlaylist.addSong(song);
                        allSongsData.add(song);
                        index++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        playlists.add(allSongsPlaylist);
        currentPlaylist = allSongsPlaylist;
        playlistTableViewController.setCurrentPlaylistSongs(allSongsPlaylist);


        FXMLLoader songsPageLoader = new FXMLLoader(Main.class.getResource("SongsPage.fxml"));
        try {
            songsPageContent = songsPageLoader.load();
            SongsPageController songsPageController = songsPageLoader.getController();
            songsPageController.displayPlaylist(playlistNode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader libraryPageLoader = new FXMLLoader(Main.class.getResource("Library.fxml"));
        try {
            libraryPageLoader.setControllerFactory(param -> new LibraryController(this));
            libraryPageContent = libraryPageLoader.load();
            libraryController = libraryPageLoader.getController();
            libraryController.setMainController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    public static void setCurrentPlaylist(Playlist currentPlaylist) {
        MainController.currentPlaylist = currentPlaylist;
    }

    public static List<File> getAllSongs() {
        return allSongs;
    }
    public static List<SongData> getAllSongsData() {
        return allSongsData;
    }

    public void setPlaylistController(PlaylistTableViewController controller) {
        this.playlistTableViewController = controller;
    }

    @FXML
    public void skipButtonClicked() {
        playlistTableViewController.nextMedia();
    }
    @FXML
    public void prevButtonClicked() {
            playlistTableViewController.prevMedia();
    }
    @FXML
    public void pauseOrPlayButtonClicked() {
        playlistTableViewController.pauseOrPlayMedia();
    }
    @FXML
    public void toggleLoopOnClick() {
        playlistTableViewController.setLooping(loopButton.isSelected());
    }
    @FXML
    public void toggleShuffleOnClick() {
        playlistTableViewController.setShuffling(shuffleButton.isSelected());
    }

    public void switchToHome() {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(homePageContent);
    }
    public void switchToSongs() {

        contentBox.getChildren().clear();
        contentBox.getChildren().add(songsPageContent);
    }
    public void switchToQueue() {}
    public void switchToLibrary() {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(libraryPageContent);
    }

    public void setContentBox(Node content) {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(content);
    }
    public void setButtonsOn() {
        this.prevButton.setDisable(false);
        this.pauseOrPlayButton.setDisable(false);
        this.skipButton.setDisable(false);
        this.loopButton.setDisable(false);
        this.shuffleButton.setDisable(false);
    }



    public void holuplemmecook() {
        colors = new Color[]{
                new Color(0.2, 0.5, 0.8, 1.0).saturate().brighter().brighter(),
                new Color(0.3, 0.2, 0.7, 1.0).saturate().brighter().brighter(),
                new Color(0.8, 0.3, 0.9, 1.0).saturate().brighter().brighter(),
                new Color(0.4, 0.3, 0.9, 1.0).saturate().brighter().brighter(),
                new Color(0.2, 0.5, 0.7, 1.0).saturate().brighter().brighter()
        };

        int spawnNodes = 70;

        for (int i = 0; i < spawnNodes; i++) {
            spawnNode(container);
        }
    }

    private void spawnNode(Pane container) {

        // create a circle node
        Circle node = new Circle(0);

        // circle shall be ignored by parent layout
        node.setManaged(false);

        // randomly pick one of the colors
        node.setFill(colors[(int) (Math.random() * colors.length)]);

        // choose a random position
        node.setCenterX(Math.random() * container.getWidth());
        node.setCenterY(Math.random() * container.getHeight() + 10);

        // add the container to the circle container
        container.getChildren().add(node);

        // create a timeline that fades the circle in and out and also moves
        // it across the screen
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(node.radiusProperty(), 0),
                        new KeyValue(node.centerXProperty(), node.getCenterX()),
                        new KeyValue(node.centerYProperty(), node.getCenterY()),
                        new KeyValue(node.opacityProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(5 + Math.random() * 5),
                        new KeyValue(node.opacityProperty(), Math.random()),
                        new KeyValue(node.radiusProperty(), Math.random() * 10)),
                new KeyFrame(
                        Duration.seconds(10 + Math.random() * 20),
                        new KeyValue(node.radiusProperty(), 0),
                        new KeyValue(node.centerXProperty(), Math.random() * container.getWidth()),
                        new KeyValue(node.centerYProperty(), Math.random() * container.getHeight()),
                        new KeyValue(node.opacityProperty(), 0))
        );

        // timeline shall be executed once
        timeline.setCycleCount(1);

        // when we are done we spawn another node
        timeline.setOnFinished(evt -> {
            container.getChildren().remove(node);
            spawnNode(container);
        });

        // finally, we play the timeline
        timeline.play();
    }

    public void setTrackSlider(JFXSlider trackSlider) {
        this.trackSlider = trackSlider;
    }


    public void updateTrackSliderValue(MediaPlayer mediaPlayer, double totalDuration) {
        trackSlider.setMin(0);
        trackSlider.setMax(totalDuration);
        System.out.println("total duration: " + totalDuration);

        // Flag to track if the slider is being dragged
        AtomicBoolean isDragging = new AtomicBoolean(false);

        trackSlider.setOnMouseClicked(event -> {
            if (!isDragging.get()) { // Check if the slider is not being dragged
                double sliderWidth = trackSlider.getWidth();
                double clickPosition = event.getX();

                // Calculate the new position based on the click position
                double newPosition = (clickPosition / sliderWidth) * totalDuration;

                // Seek to the new position
                mediaPlayer.seek(Duration.seconds(newPosition));
            }
        });

        // Handle slider dragging events
        trackSlider.setOnMousePressed(event -> isDragging.set(true));
        trackSlider.setOnMouseReleased(event -> isDragging.set(false));

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!isDragging.get()) { // Check if the slider is not being dragged
                trackSlider.setValue(newValue.toSeconds());
                System.out.println(newValue.toSeconds());
            }
        });
    }


}

