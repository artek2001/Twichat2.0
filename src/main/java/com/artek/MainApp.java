package com.artek;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;

import java.io.File;
import java.sql.*;

/**
 * Created by artek on 24.04.2018.
 */
public class MainApp extends Application {
    public static TwitchClient twitchClient;
    public static Stage stage;
    public static FXMLLoader loader;
    public static String channelName;
    public boolean isLogined = false;

    @Override
    public void init() {
        TwitchClient twitchClient = TwitchClientBuilder.init()
                .withClientId("1bt8jtsi7qcion753bqgczo9tft7kx")
                .withClientSecret("01c9pdtfwrfuza5o795h489nfxgrah")
                .withAutoSaveConfiguration(true)
                .withConfigurationDirectory(new File("config"))
                // Get your token at: https://twitchapps.com/tmi/
                .withListener(new Listeners())
                .build();
        this.twitchClient = twitchClient;

        if (twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {

            isLogined = true;

            this.channelName = twitchClient.getCredentialManager().getOAuthCredentials().get("TWITCH-IRC").getDisplayName();

            //register listeners for the channel
            twitchClient.getChannelEndpoint(channelName).registerEventListener();
            twitchClient.connect();

        } else {
            isLogined = false;

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Initializing twitch client


        stage = primaryStage;
        loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
        Parent root = loader.load();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Twichat");

        Scene sceneMain = new Scene(root, 400, 550);
        sceneMain.getStylesheets().add(getClass().getResource("notification.css").toExternalForm());

        primaryStage.setScene(sceneMain);
        if (!isLogined) {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));

            root = loader.load();
            primaryStage.setScene(new Scene(root, 400, 550));
        }


        primaryStage.show();
        FXMLLoader loaderTemp;
        System.out.println("Launched");

    }


    public static void main(String[] args) throws Exception {

        //AquaFx.style();

        launch(args);

    }


}
