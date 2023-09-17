package com.home.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SongsPageController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox songsBox;
    @FXML
    private Label totalSongsLabel;
    @FXML
    private GridPane songPane;
    @FXML
    private ToggleButton likeButton;
    private MainController mainController;
    private List<SongData> allSongsData;
    private int i;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void displayPlaylist(Node playlistNode) {
        songsBox.getChildren().add(playlistNode);
    }
}