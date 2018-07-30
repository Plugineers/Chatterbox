package com.patrickanker.chatterbox.api;

import java.util.List;

public interface Channel {

    String getID();

    /**
     * Adds a messenger to the active list of listening messengers. Called after clearing the ChannelPreJoin checks, if the add request was from a player command
     *
     * @param messenger
     * @return success
     */
    boolean addMessenger(Messenger messenger);

    /**
     * Removes a messenger to the active list of listening messengers. Called after clearing the ChannelPreLeave checks, if the remove request was from a player command
     *
     * @param messenger
     * @return success
     */
    boolean removeMessenger(Messenger messenger);

    /**
     * Verifies to see if a messenger already exists in the current list of messengers in a channel. Called upon any command/action that modifies the messenger list
     *
     * @param messenger
     * @return messenger existence
     */
    boolean hasMessenger(Messenger messenger);

    List<Messenger> getMessengers();

    MessagePayload makePayload(Messenger messenger, String message);

    boolean dispatchMessage(MessagePayload payload);

    boolean allowPayload(MessagePayload payload);

    boolean allowJoin(Messenger messenger);

    boolean allowLeave(Messenger messenger);

    String getFocusFormat();

    String getPassiveFormat();

    String getConsoleFormat();

    String permissionRoot();
}
