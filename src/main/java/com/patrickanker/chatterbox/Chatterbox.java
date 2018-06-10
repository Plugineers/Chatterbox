package com.patrickanker.chatterbox;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public final class Chatterbox {

    private static Chatterbox instance;

    private static final String LOGGING_ID = "Chatterbox";

    public static Chatterbox chatterbox() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Chatterbox();

            return instance;
        }
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
