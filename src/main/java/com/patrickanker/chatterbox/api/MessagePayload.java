package com.patrickanker.chatterbox.api;

public interface MessagePayload {

    Messenger getMessenger();
    String getMessage();
    void setMessage();
}
