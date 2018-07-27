package com.artek;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Builder;
import org.controlsfx.control.Notifications;
import sun.applet.Main;
import sun.font.FontFamily;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by artek on 24.04.2018.
 */
public class MainController implements Initializable {
    static boolean isClosed = false;
    public Stage mainStage;

    @FXML
    Button exitBtn;
    @FXML
    Button messagesBtn;
    @FXML
    Button onTopBtn;
    boolean onTop = false;
    @FXML
    public javafx.scene.control.TextArea textArea;
    @FXML
    VBox vbox;
    @FXML
    Label viewCount;
    @FXML
    ImageView imageViewCount;

    @FXML
    ScrollPane scrollPane;
    @FXML
    Label liveStatus;
    @FXML
    public static String liveStatusText = "offline";

    public static String viewCountString = "0";

    public FXMLLoader privateMessageLoader = null;
    public FXMLLoader chatsLoader = null;

    public void onEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String user = MainApp.channelName;
            String textMessage = textArea.getText();

            MainApp.twitchClient.getMessageInterface().joinChannel(MainApp.channelName);
            MainApp.twitchClient.getMessageInterface().sendMessage(MainApp.channelName, textMessage);
            Platform.runLater(() -> MainApp.loader.<MainController>getController().addLabel((Listeners.getDate() + user + ": " + textMessage), Color.BLUE));
            textArea.clear();
            event.consume();

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


        this.vbox.getChildren().addAll(label);
    }

    public void initialize(URL location, ResourceBundle resources) {
        textArea.setFocusTraversable(false);

        scrollPane.setStyle("-fx-focus-color: transparent;");
        vbox.setStyle("-fx-focus-color: transparent;");

        TextAreaSkin customTextAreaSkin = new TextAreaSkin(textArea) {
            @Override
            public void populateContextMenu(ContextMenu contextMenu) {
                super.populateContextMenu(contextMenu);
                MenuItem aboutMenuItem = new MenuItem("About");
                aboutMenuItem.setOnAction(event -> {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("about.fxml"));

                    Parent root1 = null;
                    try {
                        root1 = (Parent) fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage stage = new Stage();


                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(MainApp.stage);
                    stage.setTitle("About");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root1, 500, 400));
                    stage.showAndWait();

                });

                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(aboutMenuItem);

            }
        };
        Platform.runLater(() -> {
            mainStage = (Stage) textArea.getScene().getWindow();
        });
        textArea.setSkin(customTextAreaSkin);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/exit.png")));

        imageView.setFitWidth(16);

        imageView.setFitHeight(16);

        exitBtn.setGraphic(imageView);


        ImageView imageViewMessages = new ImageView(new Image(getClass().getResourceAsStream("/message.png")));

        imageViewMessages.setFitWidth(16);

        imageViewMessages.setFitHeight(16);

        messagesBtn.setGraphic(imageViewMessages);



        imageViewCount.setImage(new Image("view.png"));
        vbox.setSpacing(30);
        vbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue(1.0);
            }
        });
        System.out.println("MAin Controller initialized");
//        Task setViewLiveStatus = new Task<Void>() {
//
//            @Override
//            protected Void call() throws Exception {
//                while (true) {
//                    Platform.runLater(() -> {
//                        viewCount.setText(viewCountString);
//                    });
//
//
//                    Platform.runLater(() -> liveStatus.setText(liveStatusText));
//                    System.out.println("Live status has been set");
//                    System.out.println("ViewCount has been set");
//                    return  null;
//                }
//
//            }
//
//        };
//        new Thread(setViewLiveStatus).start();
        Timer taskTimer = new Timer();
        taskTimer.schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            setViewLiveStatus();
                        } catch (NullPointerException e) {
                            setViewLiveStatus();

                        }

                        Platform.runLater(() -> {
                            viewCount.setText(viewCountString);
                        });


                        Platform.runLater(() -> liveStatus.setText(liveStatusText));
                        System.out.println("Statuses have been set");

                    }

                }, 0, 5000);
        taskTimer.cancel();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////
//                try {
//                    while(true) {
//
//                        Platform.runLater(() -> viewCount.setText(String.valueOf(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getViewers().size())));
//
//                        if (MainApp.twitchClient.getStreamEndpoint().isLive(MainApp.twitchClient.getChannelEndpoint(MainApp.channelName).getChannel())) {
//                            liveStatusText = "Online";
//                        }
//                        else {
//                            liveStatusText = "Offline";
//                        }
//                        Platform.runLater(() -> liveStatus.setText(liveStatusText));
//                        System.out.println("Live status has been set");
//                        System.out.println("ViewCount has been set");
//                        Thread.sleep(20000);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        onTopBtn.getStyleClass().add("button");

        //TODO Implement push notifications
        MainApp.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isClosed = true;
                System.out.println("Stage is closed");
            }
        });
        MainApp.stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isClosed = false;
                System.out.println("Stage is opened");
            }
        });
    }

    public void setViewLiveStatus() {
        //getting view count and live status
        System.out.println(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getViewers().size());
            MainController.viewCountString = String.valueOf(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getViewers().size());
        System.out.println(MainController.viewCountString + " people are watching your stream");
        if (MainApp.twitchClient.getStreamEndpoint().isLive(MainApp.twitchClient.getChannelEndpoint(MainApp.channelName).getChannel())) {
            MainController.liveStatusText = "Online";
        } else {
            MainController.liveStatusText = "Offline";
        }
    }

    public void onTopTrue(MouseEvent mouseEvent) {
        if (onTop) {
            MainApp.stage.setAlwaysOnTop(false);
            MainApp.stage.setAlwaysOnTop(false);
            onTop = false;
            onTopBtn.setText("*");
        } else {
            MainApp.stage.setAlwaysOnTop(true);
            onTop = true;
            onTopBtn.setText("+");
        }
    }

    public void onExitTrue(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root = loader.load();
        MainApp.loader = loader;
        MainApp.stage.setScene(new Scene(root, 400, 550));
        MainApp.twitchClient.getCredentialManager().getCredentialFile().delete();
        MainApp.twitchClient.getCredentialManager().getOAuthCredentials().clear();
    }

    public void viewCountWindowOn(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("viewers.fxml"));

        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();


        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApp.stage);
        stage.setTitle("Viewers");
        stage.setResizable(false);
        stage.setScene(new Scene(root1, 500, 400));
        stage.showAndWait();
    }

    public static List<String> getViewers() {
        return MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getViewers();

    }

    public void createMessageNotification(String textMessage) {

        Notifications.create()
                .title("New Message")
                .text(textMessage)
                .show();
        System.out.println("Notification created");
    }

    public void createPrivateMessageNotification(String user, String textMessage) {
        if (privateMessageLoader != null) {
            if (user.equals(MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().fromUser)) {
                if (MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.isShowing()) {

                } else {
                    Notifications.create()
                            .title("New Message")
                            .text(textMessage)
                            .onAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.show();
                                }
                            })
                            .show();

                }
            }
        }
        else {
                Notifications.create()
                        .title("New Message")
                        .text(textMessage)

                        //Triggered when clicking on notification

                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                if (privateMessageLoader != null) {
                                    if (user.equals(MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().fromUser)) {
                                        if (MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.isShowing()) {
                                            return;
                                        }
                                        else {
                                            MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.show();
                                        }
                                    }
                                    MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().stage.close();

                                }

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("privateMessage.fxml"));
                                privateMessageLoader = fxmlLoader;
                                Parent root1;
                                try {


                                    root1 = (Parent) fxmlLoader.load();
                                    Stage stage = new Stage();


                                    stage.initModality(Modality.WINDOW_MODAL);
                                    //                            stage.initOwner(MainApp.stage);
                                    stage.setTitle("Chatting with " + user);
                                    stage.setResizable(false);
                                    stage.setScene(new Scene(root1, 500, 400));
                                    stage.show();
                                    Platform.runLater(() -> {
                                        MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().setFromUser(user);

                                        //TODO
//                                        MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().addLastMessages(
//                                                MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().fromUser
//                                        );




                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .show();
        }
        System.out.println("Private Message Notification created");
    }

    public void messagesClicked(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("chats.fxml"));
        chatsLoader = fxmlLoader;
        fxmlLoader.setController(new ChatsController());

        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();


        stage.initModality(Modality.APPLICATION_MODAL);

        //Set window position as a parent

        stage.setX(mainStage.getX() + 2);
        stage.setY(mainStage.getY() + 2);

        stage.setTitle("Chats");
        stage.setResizable(false);
        Scene scene = new Scene(root1, 390, 490);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/dialogs.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}

