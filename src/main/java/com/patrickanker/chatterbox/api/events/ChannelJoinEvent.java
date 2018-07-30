package com.patrickanker.chatterbox.api.events;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.Messenger;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ChannelJoinEvent extends Event {
    private final HandlerList handlerList = new HandlerList();

    private final Channel channel;
    private final Messenger messenger;

    public ChannelJoinEvent(Channel ch, Messenger msgr) {
        super(true);
        channel   = ch;
        messenger = msgr;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Channel getChannel() {
        return channel;
    }

    public Messenger getMessenger() {
        return messenger;
    }
}
