package com.artek;

import me.philippheuer.twitch4j.events.IListener;
import me.philippheuer.twitch4j.events.event.irc.IRCMessageEvent;

public class WhisperListener implements IListener<IRCMessageEvent> {

    public void handle(IRCMessageEvent event) {
        System.out.println(event);
    }
}
