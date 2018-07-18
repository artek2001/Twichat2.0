package com.artek;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sun.deploy.util.FXLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatsController implements Initializable {

    public ArrayList<String> labelStrings = new ArrayList<String>();

    @FXML
    VBox dialogBox;

    public Scene dialogsScene;
    public Stage chatsStage;
    public ObservableList<Label> dialogLabels = FXCollections.observableArrayList();
    ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            chatsStage = (Stage) dialogBox.getScene().getWindow();

        });


        addDialogs();


    }

    @SuppressWarnings("Duplicates")

    public void addDialogs() {

        HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> lastMessages = Listeners.allMessages;

        List<String> keysArray = new ArrayList<String>();
        keysArray.addAll(lastMessages.keySet());

        for (int i = 0; i < keysArray.size(); i++) {

            HashMap<String, HashMap<String, ArrayList<String>>> messagesFromOneUser = lastMessages.get(keysArray.get(i));
            String whoWroteLastMessage = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).keySet().iterator().next();

            ArrayList<String> messages = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).get(whoWroteLastMessage);

            String lastMessage = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).get(whoWroteLastMessage).get(messages.size() - 1);

            Label dialogLabel = new Label();

            String mainUser = keysArray.get(i);

            if (whoWroteLastMessage.equals(MainApp.channelName)) {
                dialogLabel.setText("(" + mainUser + ") " + "You: " + lastMessage);
            } else {
                dialogLabel.setText(whoWroteLastMessage + ": " + lastMessage);
            }

            dialogLabel.setWrapText(true);

            //set id for DialogLabel
            dialogLabel.setId(keysArray.get(i));


            //add label to Labels Array


            dialogLabel.setFont(new Font(18.0));
            dialogLabel.setCursor(Cursor.HAND);
            dialogLabel.setPrefHeight(50);
            dialogLabel.setStyle("-fx-background-color: #CCCCCC");
            dialogLabel.setPrefWidth(390);
            dialogLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    PrivateMessageController controller = new PrivateMessageController(dialogLabel.getId());

                    FXMLLoader privateMessageLoader = new FXMLLoader(getClass().getClassLoader().getResource("privateMessage.fxml"));

                    privateMessageLoader.setController(controller);

                    chatsStage.setTitle(mainUser);

                    MainApp.loader.<MainController>getController().privateMessageLoader = privateMessageLoader;
                    MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().setFromUser(dialogLabel.getId());


                    try {
                        AnchorPane root = privateMessageLoader.load();
                        chatsStage.setScene(new Scene(root, 390, 490));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    Parent root1 = null;
//                    try {
//                        root1 = MainApp.loader.<MainController>getController().chatsLoader.load();
//                        Scene scene = new Scene(root1, 390, 490);
//                        chatsStage.setScene(scene);
//                        chatsStage.show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            });
            Separator separatorHorizontal = new Separator(Orientation.HORIZONTAL);

            separatorHorizontal.setPrefHeight(15);
            System.out.println(dialogLabel);
            this.labelStrings.add(dialogLabel.getId());
            this.dialogLabels.add(dialogLabel);
            dialogBox.getChildren().addAll(dialogLabel, separatorHorizontal);
        }


    }

    public void updateCreatedDialogs(String user) {
        System.out.println("DIALOG LABELS ARE " + dialogLabels);
        for (int i = 0; i < this.dialogLabels.size(); i++) {
            if (this.dialogLabels.get(i).getId().equals(user)) {
                Label labelTemp = this.dialogLabels.get(i);

                String key = Listeners.allMessages.get(user).get(String.valueOf(Listeners.allMessages.get(user).size() - 1)).keySet().iterator().next();
                int listSize = Listeners.allMessages.get(user).get(String.valueOf(Listeners.allMessages.get(user).size() - 1)).get(key).size();
                String lastMessage = Listeners.allMessages.get(user).get(String.valueOf(Listeners.allMessages.get(user).size() - 1)).get(key).get(listSize - 1);

                if (!key.equals(user)) {
                    Platform.runLater(() -> {
                        labelTemp.setText("(" + user + ") " + "YOU: " + lastMessage);
                        this.dialogBox.getChildren().add(labelTemp);
                    });
                } else {
                    Platform.runLater(() -> {
                        labelTemp.setText(user + ": " + lastMessage);
                        this.dialogBox.getChildren().add(labelTemp);
                    });
                }

            }
        }
    }


    @SuppressWarnings("Duplicates")
    public void addNewDialog(String user) {
        HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> lastMessages = Listeners.allMessages;

        List<String> keysArray = new ArrayList<String>();
        keysArray.addAll(lastMessages.keySet());

        for (int i = 0; i < keysArray.size(); i++) {

            if (keysArray.get(i).equals(user)) {
                HashMap<String, HashMap<String, ArrayList<String>>> messagesFromOneUser = lastMessages.get(keysArray.get(i));
                String whoWroteLastMessage = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).keySet().iterator().next();

                ArrayList<String> messages = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).get(whoWroteLastMessage);

                String lastMessage = messagesFromOneUser.get(String.valueOf(messagesFromOneUser.size() - 1)).get(whoWroteLastMessage).get(messages.size() - 1);

                Label dialogLabel = new Label();

                String mainUser = keysArray.get(i);

                if (whoWroteLastMessage.equals(MainApp.channelName)) {
                    dialogLabel.setText("(" + mainUser + ") " + "You: " + lastMessage);
                } else {
                    dialogLabel.setText(whoWroteLastMessage + ": " + lastMessage);
                }

                dialogLabel.setWrapText(true);

                //set id for DialogLabel
                dialogLabel.setId(keysArray.get(i));


                //add label to Labels Array


                dialogLabel.setFont(new Font(18.0));
                dialogLabel.setCursor(Cursor.HAND);
                dialogLabel.setPrefHeight(50);
                dialogLabel.setStyle("-fx-background-color: #CCCCCC");
                dialogLabel.setPrefWidth(390);
                dialogLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        PrivateMessageController controller = new PrivateMessageController(dialogLabel.getId());

                        FXMLLoader privateMessageLoader = new FXMLLoader(getClass().getClassLoader().getResource("privateMessage.fxml"));

                        privateMessageLoader.setController(controller);

                        chatsStage.setTitle(mainUser);

                        MainApp.loader.<MainController>getController().privateMessageLoader = privateMessageLoader;
                        MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().setFromUser(dialogLabel.getId());


                        try {
                            AnchorPane root = privateMessageLoader.load();
                            chatsStage.setScene(new Scene(root, 390, 490));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Separator separatorHorizontal = new Separator(Orientation.HORIZONTAL);

                separatorHorizontal.setPrefHeight(15);
                System.out.println(dialogLabel);
                this.labelStrings.add(dialogLabel.getId());
                this.dialogLabels.add(dialogLabel);
                Platform.runLater(() -> {
                    dialogBox.getChildren().addAll(dialogLabel, separatorHorizontal);
                });

                break;
            }
        }


    }
}
