package com.patrickanker.chatterbox;

import com.patrickanker.chatterbox.core.ChannelManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public final class Chatterbox {

    private static final String LOGGING_ID = "Chatterbox";
    private static Chatterbox instance;

    private String executingPlugin;
    private PermissionsManager permissionsManager;
    private ChannelManager channelManager;

    public static Chatterbox chatterbox() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Chatterbox();

            return instance;
        }
    }

    public static void start(Plugin plugin) {
        // Loading logic here
        chatterbox(); // Create the instance

        instance.executingPlugin = plugin.getName();
    }

    public static void stop() {
        // Stopping logic here
    }

    public static Plugin executingPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin(chatterbox().executingPlugin);
    }

    public static PermissionsManager permissionsManager() {
        return chatterbox().permissionsManager;
    }

    public static ChannelManager channelManager() {
        return chatterbox().channelManager;
    }

    public static void logInfo(String message) {
        Bukkit.getLogger().log(Level.INFO, "[{0}] {1}", new Object[] {LOGGING_ID, message});
    }

    public static void logWarn(String message) {
        Bukkit.getLogger().log(Level.WARNING, "[{0}] {1}", new Object[] {LOGGING_ID, message});
    }

    public static void logSevere(String message) {
        Bukkit.getLogger().log(Level.SEVERE, "[{0}] {1}", new Object[] {LOGGING_ID, message});
    }
}
