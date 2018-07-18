package com.artek;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sun.applet.Main;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class PrivateMessageController implements Initializable {

    @FXML
    public TextArea sendMessageBox;

    public String fromUser = "";
    public Stage privateMessageStage;
    public Stage stage;
    @FXML
    public VBox chatBox;
    @FXML
    public Button clickBtn;
    @FXML
    public ScrollPane scrollPane;
    public DatabaseMessages databaseMessages;
    public ArrayList<String> lastFiveMessages;

    public String currentUser;

    public PrivateMessageController(String user) {
        currentUser = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chatBox.setSpacing(30);
        chatBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue(1.0);
            }
        });
        databaseMessages = new DatabaseMessages();
        Platform.runLater(() -> {
            privateMessageStage = (Stage) chatBox.getScene().getWindow();
            addLastMessages(fromUser);

        });


    }

    public void addMessage(String message, boolean isOwner) {
        Label messageLabel = new Label(message);
        System.out.println("isOWner is " + isOwner);
        messageLabel.setMaxWidth(375);
        messageLabel.setPrefWidth(375);

        messageLabel.setWrapText(true);
        messageLabel.setFont(new Font(18.0));

        if (isOwner == true) {
            messageLabel.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setTextAlignment(TextAlignment.RIGHT);
            this.chatBox.getChildren().add(messageLabel);
        } else {
            messageLabel.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setTextAlignment(TextAlignment.LEFT);
            this.chatBox.getChildren().add(messageLabel);
        }

    }

    public void addLastMessages(String user) {
        HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> allMessages = Listeners.allMessages;
        HashMap<String, HashMap<String, ArrayList<String>>> messagesCurrentDialog = allMessages.get(user);

        for (int i = 0; i < messagesCurrentDialog.size(); i++) {
            String userFrom = messagesCurrentDialog.get(String.valueOf(i)).keySet().iterator().next();

            HashMap<String, ArrayList<String>> messagesFromOneUser = messagesCurrentDialog.get(String.valueOf(i));
            ArrayList<String> messagesToDisplay = messagesFromOneUser.get(messagesFromOneUser.keySet().iterator().next());


            for (int j = 0; j < messagesToDisplay.size(); j++) {

                if (userFrom.equals(MainApp.channelName)) {
                    addMessage(messagesToDisplay.get(j), true);
                } else {
                    addMessage(messagesToDisplay.get(j), false);
                }

            }
        }
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
//
//        String[] stringListTemp = databaseMessages.gettingMessages(MainApp.channelName, fromUser).split(",");
//        ArrayList<String> fromUserArray = new ArrayList<String>(Arrays.asList(stringListTemp));
//        System.out.println(fromUserArray);
//        for (int i = 0; i < fromUserArray.size(); i++) {
//
//            addMessage(fromUserArray.get(i));
//        }
//
//        System.out.println("GETTING MESSAGES FROM" + fromUser);
    }

    @FXML
    public void sendMessage(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            MainApp.twitchClient.getMessageInterface().joinChannel(MainApp.channelName);
            MainApp.twitchClient.getMessageInterface().sendMessage(MainApp.channelName, "/w " + currentUser + " " + sendMessageBox.getText());

            event.consume();
            System.out.println("MESSAGE HAS BEEN SENT");
            Label message = new Label(sendMessageBox.getText());

            if (Listeners.allMessages.get(currentUser).get(String.valueOf(Listeners.allMessages.get(currentUser).size() - 1)).keySet().iterator().next().equals(MainApp.channelName)) {
                Listeners.allMessages.get(currentUser).get(String.valueOf(Listeners.allMessages.get(currentUser).size() - 1)).get(Listeners.allMessages.get(currentUser).get(String.valueOf(Listeners.allMessages.get(currentUser).size() - 1)).keySet().iterator().next()).add(sendMessageBox.getText());
            } else {
                ArrayList<String> arrayListTemp = new ArrayList<String>();
                arrayListTemp.add(sendMessageBox.getText());

                HashMap<String, ArrayList<String>> mapTemp = new HashMap<String, ArrayList<String>>();
                mapTemp.put(MainApp.channelName, arrayListTemp);

                Listeners.allMessages.get(currentUser).put(String.valueOf(Listeners.allMessages.get(currentUser).size()), mapTemp);
            }

            System.out.println("ALL MESSAGE 2 " + Listeners.allMessages);
            message.setAlignment(Pos.CENTER_RIGHT);
            message.setTextAlignment(TextAlignment.RIGHT);
            message.setPrefWidth(375);
            message.setMaxWidth(375);

            message.setWrapText(true);
            message.setFont(new Font(18.0));
            Platform.runLater(() -> {
                MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().chatBox.getChildren().add(message);
            });
            sendMessageBox.clear();
        }
    }

    @FXML
    public void clickBtnAction(MouseEvent event) {

        //OUTDATED STUFF YET
//        ObservableList<Node> lastFiveLabels = chatBox.getChildren();
//        ArrayList<String> listTemp = new ArrayList<String>();
//        //remove Button from label list
//        lastFiveLabels.remove(0);
//
//        System.out.println("CLICKED EVENT");
//        for (int i = 0; i < lastFiveLabels.size(); i++) {
//            if (lastFiveLabels.get(i) instanceof Label) {
//                listTemp.add(((Label) lastFiveLabels.get(i)).getText());
//            }
//        }
//        String messages = "";
//        ArrayList<String>list = new ArrayList<String>(listTemp.subList(listTemp.size() - 5, listTemp.size()));
//        for (int i = 0; i < list.size(); i++) {
//            messages = messages + list.get(i) + ",";
//        }
//        messages = messages.substring(0, messages.length() - 1);
//        new DatabaseMessages().updateDatabaseWithMessages(messages, MainApp.channelName, fromUser);

        //MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.close();
        ChatsController controller = new ChatsController();

        FXMLLoader chatsLoader = new FXMLLoader(getClass().getClassLoader().getResource("chats.fxml"));

        chatsLoader.setController(controller);


        MainApp.loader.<MainController>getController().chatsLoader = chatsLoader;
        privateMessageStage.setTitle("Chats");


        try {
            Parent root = chatsLoader.load();
            privateMessageStage.setScene(new Scene(root, 390, 490));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
