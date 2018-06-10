package com.patrickanker.chatterbox.permissions;

import com.patrickanker.chatterbox.api.Messenger;
import net.milkbowl.vault.permission.Permission;

public interface MessengerHandler {

    boolean hasPermission(Permission handler, Messenger messenger, String permission);
    boolean hasPermission(Permission handler, Messenger messenger, String permission, String worldName);
}
