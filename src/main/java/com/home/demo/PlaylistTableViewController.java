package com.home.demo;

import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.*;

public class PlaylistTableViewController implements Initializable {
    @FXML
    private TableView<SongData> playlistTableView;

    @FXML
    private TableColumn<SongData, Integer> trackNumColumn;

    @FXML
    private TableColumn<SongData, String> titleColumn;

    @FXML
    private TableColumn<SongData, String> albumColumn;

    @FXML
    private TableColumn<SongData, Boolean> likesColumn;

    @FXML
    private TableColumn<SongData, String> durationColumn;

    @FXML
    private TableColumn<SongData, Void> moreOptionsColumn;



    private  Media media;
    private  MediaPlayer mediaPlayer;
    private  int currentSongIndex;
    private  String filepath;
    private static boolean playing;
    private boolean looping;
    private boolean shuffling;
    private SongData selectedSong;
    private List<SongData> shuffledPlaylistData = new ArrayList<>();
    private Playlist likedSongsPlaylist;
    private MainController mainController;



    private static ObservableList<SongData> currentPlaylistSongs = FXCollections.observableArrayList();

    public PlaylistTableViewController(MainController mainController, JFXSlider trackSlider) {
        this.mainController = mainController;
        this.mainController.setTrackSlider(trackSlider);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trackNumColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIndex()).asObject());
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        likesColumn.setCellFactory(param -> new TableCell<SongData, Boolean>() { // Explicitly specify the types
            private final ToggleButton likeButton = new ToggleButton();

            {
                likeButton.setStyle("-fx-background-color: transparent;");
                likeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("unfilledHeart.png"))));

                // Handle button action
                likeButton.setOnAction(event -> {
                    SongData song = getTableView().getItems().get(getIndex());
                    song.setLiked(likeButton.isSelected());

                    // Change the graphic based on the liked state
                    likedSongsPlaylist = new Playlist();
                    if (likeButton.isSelected()) {
                        song.setLiked(true);
                        likeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("filledHeart.png"))));

                        likedSongsPlaylist.addSong(selectedSong);

                    } else {
                        song.setLiked(false);
                        likeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("unfilledHeart.png"))));

                        likedSongsPlaylist.removeSong(selectedSong);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {

                super.updateItem(item, empty);
                int cellIndex = getIndex();
                if (empty || cellIndex < 0) {
                    setGraphic(null);
                } else {
                    if (cellIndex < getTableView().getItems().size()) {
                        SongData song = getTableView().getItems().get(cellIndex);
                        setGraphic(likeButton);

                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        moreOptionsColumn.setCellFactory(new Callback<TableColumn<SongData, Void>, TableCell<SongData, Void>>() {
            @Override
            public TableCell<SongData, Void> call(TableColumn<SongData, Void> param) {
                return new TableCell<SongData, Void>() {
                    private final Button moreOptionsButton = new Button("⋮"); // Use the three dots character as the button label

                    {

                        moreOptionsButton.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
                        moreOptionsButton.setStyle("-fx-background-color: transparent;");
                        moreOptionsButton.getStyleClass().add("more-options-button");
                        moreOptionsButton.setOnAction(event -> showContextMenu(moreOptionsButton));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(moreOptionsButton);
                        }
                    }
                };
            }
        });

        playSelectedSong();

        // Create a custom cell factory for the titleColumn
        titleColumn.setCellFactory(new Callback<TableColumn<SongData, String>, TableCell<SongData, String>>() {
            @Override
            public TableCell<SongData, String> call(TableColumn<SongData, String> param) {
                return new TableCell<SongData, String>() {
                    private final HBox container = new HBox();
                    private final ImageView albumArtImageView = new ImageView();
                    private final VBox titleArtistContainer = new VBox();
                    private final javafx.scene.control.Label titleLabel = new javafx.scene.control.Label();
                    private final javafx.scene.control.Label artistLabel = new javafx.scene.control.Label();

                    {
                        container.setSpacing(5);
                        titleArtistContainer.getChildren().addAll(titleLabel, artistLabel);
                        albumArtImageView.setFitWidth(35);
                        albumArtImageView.setFitHeight(35);
                        container.getChildren().addAll(albumArtImageView, titleArtistContainer);
                        setGraphic(container);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        int cellIndex = getIndex();
                        if (empty || cellIndex < 0) {
                            setGraphic(null);
                        } else {
                            if (cellIndex < getTableView().getItems().size()) {
                                SongData song = getTableView().getItems().get(cellIndex);

                                titleLabel.setText(song.getTitle());
                                titleLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
                                artistLabel.setText(song.getArtist());
                                artistLabel.setStyle("-fx-text-fill: #717171; -fx-font-size: 13px;");

                                albumArtImageView.setImage(new Image(new ByteArrayInputStream(song.getAlbumArtBytes())));

                                setGraphic(container);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });


        playlistTableView.setItems(currentPlaylistSongs);
    }

    public void addSongToPlaylist(SongData song) {
        currentPlaylistSongs.add(song);
    }

    public void removeSongFromPlaylist(SongData song) {
        currentPlaylistSongs.remove(song);
    }

    public void setCurrentPlaylistSongs(Playlist currentPlaylist) {
        currentPlaylistSongs.clear();
        currentPlaylistSongs.addAll(currentPlaylist.getSongs());
    }

    public void clearPlaylist() {
        currentPlaylistSongs.clear();
    }


    private void showContextMenu(Button moreOptionsButton) {
        ContextMenu contextMenu = new ContextMenu();


        // Create a submenu for playlists (you can add more playlists dynamically)
        Menu playlistsMenu = new Menu("Add to Playlist");


        // Populate playlistsMenu with existing playlists or dynamically created playlists
        // For now, let's assume you have a list of playlist names in an ObservableList
        ObservableList<String> playlistNames = FXCollections.observableArrayList("Playlist 1", "Playlist 2", "Playlist 3");

        for (String playlistName : playlistNames) {
            MenuItem playlistItem = new MenuItem(playlistName);
            playlistItem.setOnAction(event -> {
                // Handle adding the song to the selected playlist
                String selectedPlaylist = playlistItem.getText();
                // Implement your logic to add the song to the selected playlist here
            });
            playlistsMenu.getItems().add(playlistItem);
        }


        contextMenu.getItems().addAll(playlistsMenu);
        contextMenu.setAutoHide(true);
        contextMenu.setOnShown(event -> {
                contextMenu.setX(moreOptionsButton.localToScreen(moreOptionsButton.getBoundsInLocal()).getMinX() - contextMenu.getWidth());
        });

        // Show the context menu below the button
        contextMenu.show(moreOptionsButton, Side.BOTTOM, 0, 0);
    }

    public void playSelectedSong() {
        playlistTableView.setRowFactory(tv -> {
            TableRow<SongData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    selectedSong = row.getItem();

                    if (mediaPlayer != null) {
                        mediaPlayer.dispose();
                        mediaPlayer = null;
                    }

                    currentSongIndex = currentPlaylistSongs.indexOf(selectedSong);


                    filepath = currentPlaylistSongs.get(currentSongIndex).getFilePath();
                    media = new Media(new File(filepath).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);

                    playMedia();
                    if (playing) {
                        this.mainController.setButtonsOn();

                    }
                }
            });
            return row;
        });
    }


    public void playMedia() {

        double totalDuration = currentPlaylistSongs.get(currentSongIndex).getDurationInSeconds();

        this.mainController.updateTrackSliderValue(mediaPlayer, totalDuration);


        mainController.setButtonsOn();
        mediaPlayer.setOnEndOfMedia(() -> {
            if (looping) {
                System.out.println("song shuld loop");
                mediaPlayer.seek(Duration.seconds(0));
            } else {
                mediaPlayer.dispose();
                nextMedia();
            }
        });



        mediaPlayer.play();

        playlistTableView.requestFocus(); // Focus the TableView when playing
        playlistTableView.getSelectionModel().select(currentSongIndex); // Select the currently playing song
    }

    public void nextMedia() {
        if (currentSongIndex < currentPlaylistSongs.size() - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }

        if (shuffling) {
            filepath = shuffledPlaylistData.get(currentSongIndex).getFilePath();
        } else {
            filepath = currentPlaylistSongs.get(currentSongIndex).getFilePath();
        }

        mediaPlayer.stop();
        mediaPlayer.dispose();

        media = new Media(new File(filepath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        playMedia();
    }

    public void prevMedia() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = currentPlaylistSongs.size() - 1;
        }
        mediaPlayer.stop();
        mediaPlayer.dispose();

        if (shuffling) {
            filepath = shuffledPlaylistData.get(currentSongIndex).getFilePath();
        } else {
            filepath = currentPlaylistSongs.get(currentSongIndex).getFilePath();
        }
        media = new Media(new File(filepath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playMedia();
    }
    public void setLooping(boolean looping) {
        this.looping = looping;
    }
    public void setShuffling(boolean shuffling) {
        this.shuffling = shuffling;
        shuffledPlaylistData.clear();
        shuffledPlaylistData.addAll(currentPlaylistSongs);
        System.out.println(shuffledPlaylistData.size());
        Collections.shuffle(shuffledPlaylistData, new Random());
        System.out.println("playlist has been shuffled");
    }
    public void shufflePlaylistData(boolean shuffling) {
        if (shuffling) {
            currentSongIndex = shuffledPlaylistData.indexOf(selectedSong);
            System.out.println("current song index of shuffled: " + currentSongIndex);
        }
    }

    public void pauseOrPlayMedia() {

        if (mediaPlayer != null) {
            if (playing) {
                mediaPlayer.pause();
                playing = false;
            } else {
                playMedia();
                playing = true;
            }
        }
    }
    public void switchToLikedSongs() {
        currentPlaylistSongs.clear();
        currentPlaylistSongs.addAll(likedSongsPlaylist.getSongs());
    }
    public boolean getPlaying() {
        return playing;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


}
