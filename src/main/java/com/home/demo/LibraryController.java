package com.home.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private Button likedSongsButton;

    private MainController mainController;
    private PlaylistTableViewController playlistTableViewController;

    public LibraryController(MainController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void switchToLikedSongsPlaylist() throws IOException {
        FXMLLoader likedSongsPageLoader = new FXMLLoader(Main.class.getResource("SongsPage.fxml"));
        Parent likedSongsPageContent =  likedSongsPageLoader.load();
        this.mainController.setContentBox(likedSongsPageContent);
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public void setPlaylistTableViewController(PlaylistTableViewController playlistController) {
        this.playlistTableViewController = playlistController;
    }

}
