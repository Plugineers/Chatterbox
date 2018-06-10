package com.patrickanker.chatterbox.core.messengers;

import com.patrickanker.chatterbox.api.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerMessenger implements Messenger {

    private final UUID uuid;

    public PlayerMessenger(UUID id) {
        uuid = id;
    }

    /**
     * The ID of the PlayerMessenger is the UUID of the associated player. The value of the UUID is stored locally so that Chatterbox can check to see if the player is still online.
     *
     * @return The UUID of the player in String form
     */
    @Override
    public String getID() {
        return uuid.toString();
    }

    @Override
    public String getFocus() {
        return null;
    }

    @Override
    public void assignFocus(String channelID) {

    }

    public Optional<OfflinePlayer> getOfflinePlayer() {
        return Optional.ofNullable(Bukkit.getOfflinePlayer(uuid));
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).getPlayer());
    }
}
