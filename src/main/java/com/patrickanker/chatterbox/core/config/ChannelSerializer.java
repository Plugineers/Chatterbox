package com.patrickanker.chatterbox.core.config;

import com.patrickanker.chatterbox.Chatterbox;
import org.bukkit.plugin.Plugin;

import java.io.File;

public final class ChannelSerializer {

    private static ChannelSerializer instance;

    private final File dataFile;

    private ChannelSerializer(Plugin plugin) {
        this.dataFile = plugin.getDataFolder();
    }

    public static ChannelSerializer serializer() {
        if (instance != null) {
            return instance;
        } else {
            instance = new ChannelSerializer(Chatterbox.executingPlugin());
            return instance;
        }
    }
}
