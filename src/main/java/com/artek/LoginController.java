package com.artek;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import me.philippheuer.twitch4j.endpoints.ChannelEndpoint;
import me.philippheuer.twitch4j.enums.TwitchScopes;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import sun.applet.Main;

import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by artek on 28.04.2018.
 */
public class LoginController {

    @FXML
    Button loginBtn;
    @FXML
    HBox hboxLogin;



    @FXML public void onLoginPressed() throws IOException {

        if(MainApp.twitchClient.getCredentialManager().getTwitchCredentialsForCustomKey("IRC").isPresent()) {
            MainApp.twitchClient.connect();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("hello.fxml"));
            Parent root = loader.load();
            MainApp.stage.setScene(new Scene(root,400, 550));



        }

        else {
            MainApp.twitchClient.getCredentialManager().getOAuthTwitch().requestPermissionsFor("IRC",
                    TwitchScopes.USER_READ,
                    TwitchScopes.USER_SUBSCRIPTIONS,
                    TwitchScopes.CHAT_LOGIN
            );
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
