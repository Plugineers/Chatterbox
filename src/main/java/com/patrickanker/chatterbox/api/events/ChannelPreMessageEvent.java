package com.patrickanker.chatterbox.api.events;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.MessagePayload;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ChannelPreMessageEvent extends Event implements Cancellable {
    private final HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;

    private final Channel channel;
    private MessagePayload payload;

    public ChannelPreMessageEvent(Channel ch, MessagePayload pld) {
        super(true);
        channel     = ch;
        payload     = pld;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public MessagePayload getPayload() {
        return payload;
    }

    public Channel getChannel() {
        return channel;
    }
}
