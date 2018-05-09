package com.artek;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;

import java.io.File;

/**
 * Created by artek on 24.04.2018.
 */
public class MainApp extends Application {
    public static TwitchClient twitchClient;
    public static Stage stage;
    public static FXMLLoader loader;
    public static String channelName;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Initializing twitch client
        loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
        Parent root = loader.load();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Twichat");

        Scene sceneMain = new Scene(root, 400, 550);


        primaryStage.setScene(sceneMain);
        primaryStage.show();


        TwitchClient twitchClient = TwitchClientBuilder.init()
                .withClientId("1bt8jtsi7qcion753bqgczo9tft7kx")
                .withClientSecret("8g91iubhamc5980g1jgxgpqsr0xu3c")
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

            //register listeners for the channel
            twitchClient.getChannelEndpoint(channelName).registerEventListener();
            twitchClient.connect();
            loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));

        }
        this.loader = loader;
        root = loader.load();
        primaryStage.setScene(new Scene(root, 400, 550));


    }


    public static void main(String[] args) throws Exception {
        launch(args);

    }


}
