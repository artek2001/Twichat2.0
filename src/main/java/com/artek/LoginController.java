package com.artek;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.philippheuer.twitch4j.enums.TwitchScopes;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by artek on 28.04.2018.
 */
public class LoginController {

    @FXML
    Button loginBtn;
    @FXML
    HBox hboxLogin;
    @FXML
    VBox progressVbox;

    @FXML
    Label authLabel;

    @FXML
    public void onLoginPressed() throws IOException, InterruptedException {

        if (MainApp.twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {
            MainApp.twitchClient.connect();

            MainApp.channelName = MainApp.twitchClient.getCredentialManager().getOAuthCredentials().get("TWITCH-IRC").getDisplayName();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
            Parent root = loader.load();
            MainApp.loader = loader;
            MainApp.stage.setScene(new Scene(root, 400, 550));


        } else {
            loginBtn.setDisable(true);
            loginBtn.setStyle("-fx-background-color: green;");

            MainApp.twitchClient.getCredentialManager().getOAuthTwitch().requestPermissionsFor("IRC",
                    TwitchScopes.CHAT_LOGIN,
                    TwitchScopes.CHANNEL_SUBSCRIPTIONS,
                    TwitchScopes.CHANNEL_READ,
                    TwitchScopes.USER_READ

            );
            ProgressBar pb = new ProgressBar(1.0);
            Label statusLabel = new Label("Waiting for token");

            Platform.runLater(() -> {
                MainApp.loader.<LoginController>getController().progressVbox.getChildren().addAll(pb);

                MainApp.loader.<LoginController>getController().progressVbox.getChildren()
                        .add(statusLabel);
            });

            new Timer().schedule(
                    new TimerTask() {

                        @Override
                        public void run() {
                            System.out.println("Login Timer started");
                            if (!MainApp.twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {
                                System.out.println("Waiting for token");

                            } else {
                                Platform.runLater(() -> {
                                    MainApp.loader.<LoginController>getController().progressVbox.getChildren().remove(pb);
                                    statusLabel.setText("Press login button to continue");
                                    statusLabel.setStyle("-fx-border-color: black");
                                    loginBtn.setStyle("-fx-border-color: black");
                                    authLabel.setText("Successful Authorization");
                                });
                                loginBtn.setDisable(false);

                                loginBtn.setStyle("-fx-background-color: #f8f8ff;");
                                this.cancel();
                            }
                        }

                    }, 0, 5000);


//            new Thread(new Task<>() {
//                @Override
//                protected Object call() throws Exception {
//
//                    return null;
//                }
//            }).start();
        }
    }


}


//        boolean loop = true;
//        while(loop) {
//            if(twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {
//                loop = false;
//            }
//        }
//        twitchClient.connect();
