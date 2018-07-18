package com.artek;

import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;

import javax.swing.text.html.ImageView;
import java.io.File;

public class AppPreloader extends Preloader {

    ProgressBar bar;
    Stage stage;
    Scene scene;
    private Scene createPreloaderScene() {
        BorderPane p = new BorderPane();
        p.getChildren().add(new javafx.scene.image.ImageView(getClass().getClassLoader().getResource("twichatlogo.png").toExternalForm()));
        scene = new Scene(p, 600, 400);
        return scene;
    }

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(createPreloaderScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {

    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {

            stage.hide();
        }
    }
}
