package com.patrickanker.chatterbox.core.formatting;

import com.patrickanker.chatterbox.api.Channel;
import com.patrickanker.chatterbox.api.Messenger;

import java.util.Optional;

public interface FormattingContext {

    /**
     * Gets the raw, unprocessed message
     *
     * @return message
     */
    String getRawMessage();

    /**
     * Gets the current in-process message
     *
     * @return
     */
    String getWorkingMessage();

    /**
     * Updates the in-process message
     *
     */
    void setWorkingMessage();

    /**
     * If the message has a reference to a channel, this will contain it.
     *
     * @return channel, optionally
     */
    Optional<Channel> getTargetChannel();

    /**
     * If a messenger is relevant to this formatting context, this will containit
     *
     * @return messenger, optionally
     */
    Optional<Messenger> getMessenger();
}
