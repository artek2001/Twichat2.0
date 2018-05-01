package com.artek;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;
import me.philippheuer.twitch4j.auth.OAuthTwitch;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.TwitchScopes;

import java.awt.*;
import java.io.File;

import static javafx.application.Application.launch;

/**
 * Created by artek on 24.04.2018.
 */
public class MainApp extends Application{
    public static TwitchClient twitchClient;
    public static Stage stage;
    public static FXMLLoader loader;
    public static String channelName;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Initializing twitch client

        TwitchClient twitchClient = TwitchClientBuilder.init()
                .withClientId(";")
                .withClientSecret(";")
                .withAutoSaveConfiguration(true)
                .withConfigurationDirectory(new File("config"))
                 // Get your token at: https://twitchapps.com/tmi/
                .withListener(new Listeners())
                .build();
        this.twitchClient = twitchClient;
        this.stage = primaryStage;








        FXMLLoader loader;
        System.out.println("Launched");
        if (twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {
            this.channelName = twitchClient.getCredentialManager().getOAuthCredentials().get("TWITCH-IRC").getDisplayName();
            twitchClient.getChannelEndpoint(channelName).registerEventListener();
            twitchClient.connect();
            loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
        }
        else {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        }
        this.loader = loader;
        Parent root = loader.load();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Twichat");

        Scene sceneMain = new Scene(root, 400, 550);

        primaryStage.setScene(sceneMain);
        primaryStage.show();



    }
    public static void main(String[] args) throws Exception {
        launch(args);

    }
}
