package com.home.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LikedSongsPageController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox songsBox;
    @FXML
    private Label totalSongsLabel;
    private MainController mainController;
    private int i;

    public LikedSongsPageController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}