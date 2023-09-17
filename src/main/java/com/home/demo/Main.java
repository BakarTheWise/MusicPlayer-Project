package com.home.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader musicPlayerLoader = new FXMLLoader(Main.class.getResource("MusicPlayer.fxml"));
        Parent musicPlayerRoot = musicPlayerLoader.load();
        Scene scene = new Scene(musicPlayerRoot);
        stage.setTitle("ORKESTRA");
        stage.setScene(scene);

//        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}