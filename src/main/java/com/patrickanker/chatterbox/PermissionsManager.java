package com.patrickanker.chatterbox;

import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.core.messengers.ConsoleMessenger;
import com.patrickanker.chatterbox.core.messengers.PlayerMessenger;
import com.patrickanker.chatterbox.util.OptionalConsumer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;

import java.util.Optional;

public final class PermissionsManager {
    private static final String ALL_WORLDS = "*";

    private Permission permissionHandler = null;
    private boolean servicesChecked      = false;

    public PermissionsManager() {
        getHandler().ifPresent(h -> Chatterbox.logInfo("Captured Vault permissions"))
                .ifNotPresent(() -> Chatterbox.logInfo("Defaulting to Bukkit \"superperms\""));
    }

    private OptionalConsumer<Permission> getHandler() {
        // Prevents multiple calls to Bukkit, which may be expensive
        if (!servicesChecked) {
            permissionHandler = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
            servicesChecked = true;
        }

        return OptionalConsumer.of(Optional.ofNullable(permissionHandler));
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

        if (getHandler().isPresent()) {
            Permission handler = getHandler().get();

            if (worldName.equals(ALL_WORLDS)) {
                if (pMsgr.getPlayer().isPresent()) {
                    return handler.playerHas(pMsgr.getPlayer().get(), permission);
                } else {
                    if (pMsgr.getOfflinePlayer().isPresent()) {
                        return handler.playerHas(null, pMsgr.getOfflinePlayer().get(), permission);
                    } else {
                        return false;
                    }
                }
            } else {
                if (pMsgr.getOfflinePlayer().isPresent()) {
                    return handler.playerHas(worldName, pMsgr.getOfflinePlayer().get(), permission);
                } else {
                    return false;
                }
            }
        } else {
            // Default to Bukkit permissions. Per-world permissions not checked
            if (pMsgr.getPlayer().isPresent()) {
                return pMsgr.getPlayer().get().hasPermission(permission);
            } else {
                return false;
            }
        }
    }

    public boolean inGroup(PlayerMessenger pMsgr, String groupName) {
        if (getHandler().isPresent()) {
            if (pMsgr.getPlayer().isPresent()) {
                return getHandler().get().playerInGroup(pMsgr.getPlayer().get(), groupName);
            } else {
                // This getOfflinePlayer().get() call is acceptable since the Player is present
                return getHandler().get().playerInGroup(null, pMsgr.getOfflinePlayer().get(), groupName);
            }
        } else {
            return hasPermission(pMsgr, "group." + groupName);
        }
    }
}
