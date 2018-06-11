package com.patrickanker.chatterbox.util.commands;

import com.patrickanker.chatterbox.Chatterbox;
import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.core.messengers.PlayerMessenger;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public abstract class DynamicPluginCommand extends Command implements PluginIdentifiableCommand {

    private String owningPlugin;

    private int[] argbounds    = {-1, -1};
    private boolean playerOnly = false;

    public DynamicPluginCommand() {
        super("", "", "", new ArrayList<>());
    }

    protected void setOwningPlugin(String plg) {
        owningPlugin = plg;
    }

    protected void setArgumentBounds(@NotNull int[] minmax) {
        argbounds = minmax;
    }

    public int[] getArgumentBounds() {
        return argbounds;
    }

    public int getMinimumNumberOfArguments() {
        return argbounds[0];
    }

    public int getMaximumNumberOfArguments() {
        return argbounds[1];
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    protected void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    @Override
    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin(owningPlugin);
    }

    public boolean checkPermissions(CommandSender target, Optional<DynamicSubcommand> subcommand) {
        if (subcommand.isPresent()) {
            return testPermissionString(target,getPermission() + "." + subcommand.get().permission());
        } else {
            return testPermissionString(target, getPermission());
        }
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return testPermissionString(target, getPermission());
    }

    private boolean testPermissionString(CommandSender target, String permission) {
        // Firstly, determine if the sender is the Console
        if (target instanceof ConsoleCommandSender) {
            return true;
        } else {
            if (permission.equals("")) {
                return true;
            } else {
                // Fetch the player's Messenger instance from the ChannelManager. It should exist, otherwise log the failure and return false. This is unexpected behavior.
                Optional<Messenger> pMsgrOpt = Chatterbox.channelManager().getMessenger(((Player) target).getUniqueId().toString());

                if (!pMsgrOpt.isPresent()) {
                    Chatterbox.logSevere("Unexpected missing PlayerMessenger instance from Player \"" + target.getName() + "\"");
                    return false;
                }

                PlayerMessenger pMsgr = (PlayerMessenger) pMsgrOpt.get();

                // Check global permission first
                if (Chatterbox.permissionsManager().hasPermission(pMsgr, permission)) {
                    return true;
                } else {
                    return Chatterbox.permissionsManager().hasPermission(pMsgr, permission, ((Player) target).getWorld().getName());
                }
            }
        }
    }

    public static <T extends DynamicPluginCommand> Optional<DynamicSubcommand> subcommandFromArgs(T t, String[] args) {
        return Arrays.asList(t.getClass().getAnnotationsByType(DynamicSubcommand.class))
                .stream()
                .filter(sub -> Arrays.asList(sub.aliases()).stream()
                        .anyMatch(al -> al.equals(args[1])))
                .findFirst();
    }
}
