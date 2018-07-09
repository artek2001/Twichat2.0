package com.artek;

import javafx.application.Platform;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.IRCMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.PrivateMessageEvent;
import org.controlsfx.control.Notifications;
import sun.applet.Main;

import java.util.*;

/**
 * Created by artek on 28.04.2018.
 */
public class Listeners {

    public static HashMap<String, ArrayList<String>> fiveLastMessages = new HashMap<String, ArrayList<String>>();
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
        System.out.println("MESSAGE IS " + event.getMessage());
        javafx.scene.paint.Color color = getRandomColor();

//            String dateStringResult = dateString.replaceAll("\\A([^\\r\\n]*\\n){1}", "");
        if (MainController.isClosed) {

        }
        Platform.runLater(() -> MainApp.loader.<MainController>getController().addLabel((getDate() + user + ": " + event.getMessage()), color));




    }


    @EventSubscriber
    public void onChannelMessage(PrivateMessageEvent event) {
        fromUser = event.getUser().getDisplayName();
        System.out.println("PRIVATE MESSAGE IS" + event.getMessage());
        Platform.runLater(() -> {

            MainApp.loader.<MainController>getController().createPrivateMessageNotification(event.getUser().getDisplayName(), event.getMessage());

        });



        addNewMessages(event.getUser().getDisplayName(), event.getMessage());
        System.out.println("MESSAGES FROM USER ARE: " + Listeners.fiveLastMessages.get(event.getUser().getDisplayName()));
        if (MainApp.loader.<MainController>getController().privateMessageLoader != null) {
            Platform.runLater(() -> MainApp.loader.<MainController>getController().privateMessageLoader.<PrivateMessageController>getController().addMessage(event.getMessage()));
        }
        else {
            System.out.println("NO PrivateMessageController present");
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

    public void addNewMessages(String user, String message) {
        if (fiveLastMessages.containsKey(user)) {
            fiveLastMessages.get(user).add(message);
            if (fiveLastMessages.get(user).size() > 5) {
                fiveLastMessages.get(user).remove(0);
            }
        }
        else {
            fiveLastMessages.put(user, new ArrayList<String>());
            fiveLastMessages.get(user).add(message);
        }
    }

}
