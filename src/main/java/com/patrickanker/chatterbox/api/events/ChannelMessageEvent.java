package com.patrickanker.chatterbox.api.events;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.MessagePayload;
import com.patrickanker.chatterbox.api.Messenger;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ChannelMessageEvent extends Event {

    private final HandlerList handlerList = new HandlerList();

    private final Channel channel;
    private final MessagePayload payload;
    private final long timestamp;

    public ChannelMessageEvent(Channel ch, MessagePayload pld) {
        super(true);

        channel   = ch;
        payload   = pld;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Channel getChannel() {
        return channel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessagePayload getPayload() {
        return payload;
    }
}
