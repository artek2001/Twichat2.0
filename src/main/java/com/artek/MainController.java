package com.artek;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import sun.applet.Main;

import java.awt.*;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by artek on 24.04.2018.
 */
public class MainController implements Initializable {



    @FXML public javafx.scene.control.TextArea textArea;
    @FXML VBox vbox;
    @FXML Label viewCount;
    @FXML
    ImageView imageViewCount;
    @FXML ScrollPane scrollPane;
    @FXML Label liveStatus;
    @FXML
    String liveStatusText = "offline";
    public void onEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String user = MainApp.channelName;
            String textMessage = textArea.getText();
            textArea.clear();
            MainApp.twitchClient.getMessageInterface().joinChannel(MainApp.channelName);
            MainApp.twitchClient.getMessageInterface().sendMessage(MainApp.channelName, textMessage);
            Platform.runLater(() -> MainApp.loader.<MainController>getController().addLabel((Listeners.getDate() + user + ": " + textMessage), Color.BLUE));

        }


    }

    public void addLabel(String text, javafx.scene.paint.Color color) {

        System.out.println(text);
        Label label = new Label(text);
        label.setMaxWidth(390);



//        if(hbox.getChildren().size() >= 1) {
//            label.setLayoutX(hbox.getChildren().get(0).getLayoutX());
//            double yCordinate = hbox.getChildren().get(hbox.getChildren().size()-1).getBoundsInParent().getHeight();
//            label.setLayoutY(hbox.getChildren().get(hbox.getChildren().size()-1).getLayoutY() + 50 + yCordinate);
//
//        }

        label.setWrapText(true);



        final double MAX_FONT_SIZE = 18.0; // define max font size you need
        label.setFont(new javafx.scene.text.Font(MAX_FONT_SIZE)); // set to Label
//        label.setTextFill(javafx.scene.paint.Color.color(Math.random(), Math.random(), Math.random()));
        label.setLayoutX(0);
        label.setTextFill(color);



        this.vbox.getChildren().add(label);
    }
    public void initialize(URL location, ResourceBundle resources) {
        imageViewCount.setImage(new Image("view.png"));
        vbox.setSpacing(30);
        vbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue(1.0);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
//
                try {
                    while(true) {

                        Platform.runLater(() -> viewCount.setText(String.valueOf(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getViewers().size())));

                        if (MainApp.twitchClient.getStreamEndpoint().isLive(MainApp.twitchClient.getChannelEndpoint(MainApp.channelName).getChannel())) {
                            liveStatusText = "Online";
                        }
                        else {
                            liveStatusText = "Offline";
                        }
                        Platform.runLater(() -> liveStatus.setText(liveStatusText));
                        System.out.println("Live status has been set");
                        System.out.println("ViewCount has been set");
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
