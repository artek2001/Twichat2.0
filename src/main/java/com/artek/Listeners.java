package com.artek;

import javafx.application.Platform;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.irc.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.irc.PrivateMessageEvent;
import org.controlsfx.control.Notifications;

import java.util.Date;
import java.util.Random;

/**
 * Created by artek on 28.04.2018.
 */
public class Listeners {

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
            Platform.runLater(() -> MainApp.loader.<MainController>getController().createMessageNotification(getDate() + user + ": " + event.getMessage()));
        }
        Platform.runLater(() -> MainApp.loader.<MainController>getController().addLabel((getDate() + user + ": " + event.getMessage()), color));




    }

    @EventSubscriber
    public void onPrivateMessage(PrivateMessageEvent event) {
        System.out.println("Private Message sent");
        System.out.println(event.getMessage());
    }
}
