package com.patrickanker.chatterbox.api;

public interface Messenger {

    String getID();

    String getFocus();

    void assignFocus(String channelID);

}
