package com.patrickanker.chatterbox.core.channels;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.ChannelType;
import com.patrickanker.chatterbox.api.MessagePayload;
import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.core.config.ConfigOption;
import com.patrickanker.chatterbox.core.ChannelManager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ChannelType("chat")
public final class ChatChannel implements Channel, Serializable {

    private transient final List<Messenger> listeners = new LinkedList<>();
    private transient final String channelID;

    /**
     *
     * The name of the channel, used as a public shorthand for the ID. There cannot be two channels of the same name.
     *
     */
    @ConfigOption("name") private String displayName = "channel";

    /**
     *
     * The default channel to which everyone joins on login.
     *
     */
    @ConfigOption("default") private boolean defaultChannel = false;

    /**
     *
     * Set this to true to make the channel check for "<channel permission root>.send" when a message is being preprocessed.
     *
     */
    @ConfigOption("promoted") private boolean promoted = false;

    /**
     *
     * Set this to a positive integer value to limit the propagation of the message to a circle around the Messenger.
     * Does not function for ConsoleMessenger. Only enabled when the "world" setting is not "*" and is a valid world name.
     *
     */
    @ConfigOption("radius") private int messageRadius = -1;

    /**
     *
     * Set this to the name of a valid world to enable world-based channels.
     * Neither the default channel nor HelpOp channel can be world-based.
     *
     */
    @ConfigOption("world") private String world = "*";

    /**
     *
     * Enables welcome messages sent the Messenger who just joined the channel. Will only work if "welcome-message" is not empty.
     *
     */
    @ConfigOption("welcome-enable") private boolean welcomeEnable = true;

    /**
     *
     * Enables welcome messages sent the Messenger who just joined the channel. Will only work if "welcome-message" is not empty.
     *
     */
    @ConfigOption("welcome-message") private String welcomeMessage = "";

    public ChatChannel() {
        this.channelID = ChannelManager.generateID();
    }

    @Override
    public String getID() {
        return channelID;
    }

    @Override
    public boolean addMessenger(Messenger messenger) {
        return false;
    }

    @Override
    public boolean removeMessenger(Messenger messenger) {
        return false;
    }

    @Override
    public boolean hasMessenger(Messenger messenger) {
        return false;
    }

    @Override
    public List<Messenger> getMessengers() {
        return listeners;
    }

    @Override
    public MessagePayload makePayload(Messenger messenger, String message) {
        return null;
    }

    @Override
    public boolean dispatchMessage(MessagePayload payload) {
        return false;
    }

    @Override
    public boolean allowPayload(MessagePayload payload) {
        return false;
    }

    @Override
    public boolean allowJoin(Messenger messenger) {
        return false;
    }

    @Override
    public boolean allowLeave(Messenger messenger) {
        return false;
    }

    @Override
    public String getFocusFormat() {
        return null;
    }

    @Override
    public String getPassiveFormat() {
        return null;
    }

    @Override
    public String getConsoleFormat() {
        return null;
    }

    @Override
    public String permissionRoot() {
        return "chatterbox.channel.chat." + displayName.toLowerCase();
    }
}
