package com.patrickanker.chatterbox.api.events;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.Messenger;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ChannelPreJoinEvent extends Event implements Cancellable {

    private final HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;

    private final Channel channel;
    private final Messenger messenger;

    public ChannelPreJoinEvent(Channel ch, Messenger msgr) {
        super(true);
        channel   = ch;
        messenger = msgr;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
