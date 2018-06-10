package com.patrickanker.chatterbox.permissions;

import com.patrickanker.chatterbox.Chatterbox;
import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.core.messengers.ConsoleMessenger;
import com.patrickanker.chatterbox.core.messengers.PlayerMessenger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class PermissionsManager {

    private Permission handler;
    private final Map<Class<? extends Messenger>, MessengerHandler> knownMessengerTypes = new HashMap<>();

    private static final String ALL_WORLDS = "*";

    public PermissionsManager(String plgName) {
        RegisteredServiceProvider<Permission> serviceProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        handler = serviceProvider.getProvider();

        if (handler != null) {
            Chatterbox.logInfo("Captured Vault permissions");
        } else {
            Chatterbox.logInfo("Defaulting to Bukkit \"superperms\"");
        }
    }

    public void registerMessengerHandler(Class<? extends Messenger> msgrType, MessengerHandler msgrHandler) {
        if (!knownMessengerTypes.containsKey(msgrType)) {

        }
    }

    public boolean hasPermission(Messenger messenger, String permission) {
        return hasPermission(messenger, permission, ALL_WORLDS);
    }

    public boolean hasPermission(Messenger messenger, String permission, String worldName) {
        // Check if the messenger is from the console or from a player
        if (messenger instanceof ConsoleMessenger) {
            return true;
        }

        PlayerMessenger pMsgr = (PlayerMessenger) messenger;

        if (handler != null) {
            if (worldName.equals(ALL_WORLDS)) {
                return handler.playerHas(pMsgr.getPlayer(), permission);
            } else {
                return handler.playerHas(worldName, pMsgr.getOfflinePlayer(), permission);
            }
        } else {
            // Default to Bukkit. Ensure the Player for the Messenger is not null before checking permissions
            if (pMsgr.getPlayer() != null) {
                return pMsgr.getPlayer().hasPermission(permission);
            } else {
                return false;
            }
        }
    }

    public boolean inGroup(PlayerMessenger pMsgr, String groupName) {
        if (handler != null) {
            if (pMsgr.getPlayer() != null) {
                return handler.playerInGroup(pMsgr.getPlayer(), groupName);
            } else {
                return handler.playerInGroup(null, pMsgr.getOfflinePlayer(), groupName);
            }
        } else {
            return hasPermission(pMsgr, "group." + groupName);
        }
    }
}
