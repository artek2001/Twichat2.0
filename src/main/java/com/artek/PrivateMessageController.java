package com.artek;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PrivateMessageController implements Initializable {
    public String fromUser = "";
    public Stage stage;
    @FXML
    public VBox chatBox;
    @FXML
    public Button clickBtn;
    @FXML
    public ScrollPane scrollPane;
    public DatabaseMessages databaseMessages;
    public ArrayList<String> lastFiveMessages;


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
            stage = (Stage) chatBox.getScene().getWindow();


        });

    }

    public void addMessage(String message) {
        Label messageLabel = new Label(message);

        messageLabel.setMaxWidth(600);

        messageLabel.setWrapText(true);
        messageLabel.setFont(new Font(18.0));
        this.chatBox.getChildren().add(messageLabel);

    }

    public void addLastMessages(String user) {
        ArrayList<String> messages = Listeners.fiveLastMessages.get(user);

        for (int i = 0; i < messages.size(); i++) {
            addMessage(messages.get(i));
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

        MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.close();

    }
}
