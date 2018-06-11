package com.patrickanker.chatterbox.core;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.ChannelType;
import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.api.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.io.Serializable;
import java.util.*;

public final class ChannelManager {

    private final LinkedList<Channel> registeredChannels = new LinkedList<>();
    private final ArrayList<Class> registeredTypes       = new ArrayList<>();
    private final ArrayList<Messenger> knownMessengers   = new ArrayList<>();

    private static ChannelManager singleton;

    public static ChannelManager channelManager() {
        if (singleton != null) {
            return singleton;
        } else {
            singleton = new ChannelManager();
            return singleton;
        }
    }

    public boolean hasChannel(Channel ch) {
        for (Channel channel : registeredChannels) {
            if (ch.getID().equals(channel.getID())) {
                return true;
            }
        }

        return false;
    }

    public Channel makeChannel(Class cls, Map<String, Object> options) throws Exception {
        // First check to make sure that the class is valid
        if (!cls.isAssignableFrom(Channel.class)) {
            throw new Exception("Class must implement Channel");
        }

        // Second, check to verify that the appropriate ChannelType is set
        if (!cls.isAnnotationPresent(ChannelType.class)) {
            throw new Exception("Channel Type must be declared on Channel");
        }

        if (cls.isAssignableFrom(Serializable.class)) {
            // Handle class as serialized and load from storage file, if it exists
            // Otherwise, use options to write initial configuration of channel
            // TODO
        } else {
            // No data stored for channel
            return (Channel) cls.newInstance();

        }
    }

    private String generateID() {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder idBuilder = new StringBuilder();
        Random rnd = new Random();

        while (idBuilder.length() < 15) {
            int i = (int) (rnd.nextFloat() * CHARS.length());
            idBuilder.append(CHARS.charAt(i));
        }

        return idBuilder.toString();
    }

    private boolean addChannel(Channel ch) {

    }

    public boolean hasMessenger(String uuid) {
        return knownMessengers.stream()
                .anyMatch(msgr -> msgr.getID().equals(uuid));
    }

    public Optional<Messenger> getMessenger(String uuid) {
        return knownMessengers.stream()
                .filter(msgr -> msgr.getID().equals(uuid))
                .findFirst();
    }

    public void addMessenger(Messenger msgr) {
        if (!hasMessenger(msgr.getID())) {
            knownMessengers.add(msgr);
        }
    }

    public void removeMessenger(String uuid) {
        if (hasMessenger(uuid)) {
            knownMessengers.remove(getMessenger(uuid).get());
        }
    }

    // Event handling
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelPreCreate(ChannelPreCreateEvent e) {

    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelCreate(ChannelCreateEvent e) {

    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelPreMessage(ChannelPreMessageEvent e) {

        // Permissions should be handled by the specific Channel implementation
        if (!e.isCancelled()) {

            if (e.getChannel().allowPayload(e.getPayload())) {
                Bukkit.getServer().getPluginManager().callEvent(new ChannelMessageEvent(e.getChannel(), e.getPayload()));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelMessage(ChannelMessageEvent e) {

        // Presumably, this is all that's required
        e.getChannel().dispatchMessage(e.getPayload());
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelPreJoin(ChannelPreJoinEvent e) {

        // Permissions should be handled by the specific Channel implementation
        if (!e.isCancelled()) {

            if (e.getChannel().allowJoin(e.getMessenger())) {
                Bukkit.getServer().getPluginManager().callEvent(new ChannelJoinEvent(e.getChannel(), e.getMessenger()));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelJoin(ChannelJoinEvent e) {

    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelPreLeave(ChannelPreLeaveEvent e) {

        // Permissions should be handled by the specific Channel implementation
        if (!e.isCancelled()) {

            if (e.getChannel().allowLeave(e.getMessenger())) {
                Bukkit.getServer().getPluginManager().callEvent(new ChannelJoinEvent(e.getChannel(), e.getMessenger()));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChannelLeave(ChannelLeaveEvent e) {

    }
}
