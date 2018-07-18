package com.artek;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.IRCMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.PrivateMessageEvent;
import org.controlsfx.control.Notifications;
import org.omg.CORBA.INTERNAL;
import sun.applet.Main;

import java.net.URL;
import java.util.*;

/**
 * Created by artek on 28.04.2018.
 */
public class Listeners {


    public static HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> allMessages = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();

    public static String fromUser = "";

    public enum Color {
        Blue,
        BlueViolet,
        CadetBlue,
        Chocolate,
        Coral,
        DodgerBlue,
        Firebrick,
        GoldenRod,
        Green,
        HotPink,
        OrangeRed,
        Red,
        SeaGreen,
        SpringGreen,
        YellowGreen
    }

    public static String getDate() {
        Date date = new Date();

        String dateString = "[" + date.toLocaleString().replaceAll("^(\\S+\\s+){1}", "") + "]" + " ";
        return dateString;
    }

    private static final Color[] COLORS = Color.values();
    private static final int SIZE = COLORS.length;
    private static final Random RANDOM = new Random();

    public static javafx.scene.paint.Color getRandomColor() {
        final String myCOLOR = COLORS[RANDOM.nextInt(SIZE)].toString();

        return javafx.scene.paint.Color.valueOf(myCOLOR.toUpperCase());
    }


    @EventSubscriber
    public void onChannelMessage(ChannelMessageEvent event) {
        String user = event.getUser().getDisplayName();

        javafx.scene.paint.Color color = getRandomColor();

//            String dateStringResult = dateString.replaceAll("\\A([^\\r\\n]*\\n){1}", "");
        if (MainController.isClosed) {

        }
        Platform.runLater(() -> MainApp.loader.<MainController>getController().addLabel((getDate() + user + ": " + event.getMessage()), color));




    }


    @EventSubscriber
    public void onChannelMessage(PrivateMessageEvent event) {
        fromUser = event.getUser().getName();
        System.out.println("DISPLAY NAME IS " + event.getUser().getDisplayName());
        System.out.println("NAME IS " + event.getUser().getName());

        //TODO NOTIFICATIONS
//        Platform.runLater(() -> {
//
//            MainApp.loader.<MainController>getController().createPrivateMessageNotification(event.getUser().getName(), event.getMessage());
//
//        });



        addNewMessages(event.getUser().getName(), event.getMessage());

        if (MainApp.loader.<MainController>getController().privateMessageLoader != null && (MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().fromUser.equals(event.getUser().getName()))) {
            Platform.runLater(() -> MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().addMessage(event.getMessage(), false));
        }
        else {
            System.out.println("NO PrivateMessageController present");
        }

        if (MainApp.loader.<MainController>getController().chatsLoader.<ChatsController>getController() != null) {
            MainApp.loader.<MainController>getController().chatsLoader.<ChatsController>getController().updateCreatedDialogs(event.getUser().getName());
        }

        System.out.println("ALL MESSAGES ARE " + allMessages);


        if (!MainApp.loader.<MainController>getController().chatsLoader.<ChatsController>getController().labelStrings.contains(event.getUser().getName())) {
            MainApp.loader.<MainController>getController().chatsLoader.<ChatsController>getController().addNewDialog(event.getUser().getName());
        }

//        System.out.println(event.getUser().getDisplayName());
//        DatabaseMessages databaseMessages = new DatabaseMessages();
//        databaseMessages.updateDatabaseWithMessages(getFiveLastMessagesPlusNewMessage(MainApp.channelName, fromUser, event.getMessage()), MainApp.channelName, fromUser);
//        System.out.println("USER PRESENT IN DB " + databaseMessages.isUserPresentInDatabase(fromUser, MainApp.channelName));

    }

//    public String getFiveLastMessagesPlusNewMessage(String owner, String fromUser, String newMessage) {
//        String messages = new DatabaseMessages().gettingMessages(owner, fromUser);
//        messages = messages + "," + newMessage;
//        if (messages.split(",").length > 5) {
//            String[] messagesTemp = messages.split(",");
//            String[] messagesResult = Arrays.copyOfRange(messagesTemp, 1, messagesTemp.length);
//
//            String returnMessage = "";
//            for (int i = 0; i < messagesResult.length; i++) {
//                returnMessage = returnMessage + messagesResult[i] + ",";
//            }
//            returnMessage = returnMessage.substring(0, returnMessage.length() - 1);
//            return returnMessage;
//        }
//        else {
//            return messages;
//        }
//    }

    @SuppressWarnings("Duplicates")
    public void addNewMessages(String user, String message) {
        if (allMessages.size() > 0 ){
            HashMap<String, HashMap<String, ArrayList<String>>> lastMessages = allMessages.get(user);
            if (lastMessages.size() > 0) {
                if (lastMessages.get(String.valueOf(lastMessages.size() - 1)).keySet().iterator().next().equals(user)) {

                    HashMap<String, ArrayList<String>> messages = lastMessages.get(String.valueOf(lastMessages.size() - 1));

                    String firstKey = messages.keySet().iterator().next();

                    messages.get(firstKey).add(message);

                }
                else {
                    String newKey = String.valueOf(lastMessages.size());
                    ArrayList<String> messagesArray = new ArrayList<String>();
                    messagesArray.add(message);
                    HashMap<String, ArrayList<String>> mapTemp = new HashMap<String, ArrayList<String>>();
                    mapTemp.put(user, messagesArray);
                    lastMessages.put(newKey, mapTemp);
                }
            }

            else {

                String newKey = String.valueOf(lastMessages.size());
                ArrayList<String> messagesArray = new ArrayList<String>();
                messagesArray.add(message);
                HashMap<String, ArrayList<String>> mapTemp = new HashMap<String, ArrayList<String>>();
                mapTemp.put(user, messagesArray);
                lastMessages.put(newKey, mapTemp);
            }
        }
        else {
            ArrayList<String> messages = new ArrayList<String>();
            messages.add(message);
            HashMap<String, ArrayList<String>> map1 = new HashMap<String, ArrayList<String>>();
            map1.put(user, messages);
            HashMap<String, HashMap<String, ArrayList<String>>> map2 = new HashMap<String, HashMap<String, ArrayList<String>>>();
            map2.put("0", map1);

            allMessages.put(user, map2);
        }

    }

}
