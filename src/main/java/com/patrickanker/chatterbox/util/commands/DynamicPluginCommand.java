package com.patrickanker.chatterbox.util.commands;

import com.patrickanker.chatterbox.Chatterbox;
import com.patrickanker.chatterbox.api.Messenger;
import com.patrickanker.chatterbox.core.messengers.PlayerMessenger;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public abstract class DynamicPluginCommand extends Command implements PluginIdentifiableCommand {
    private static final String[] HELP_ARGS = {"help", "h", "?"};

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

    protected boolean argsInBounds(String[] args) {
        if (getMinimumNumberOfArguments() > 0 && args.length < getMinimumNumberOfArguments()) {
            return false;
        }

        if (getMaximumNumberOfArguments() > 0 && args.length > getMaximumNumberOfArguments()) {
            return false;
        }

        return true;
    }

    protected int getMinimumNumberOfArguments() {
        return argbounds[0];
    }

    protected int getMaximumNumberOfArguments() {
        return argbounds[1];
    }

    protected boolean isPlayerOnly() {
        return playerOnly;
    }

    protected void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    abstract boolean doCommand(DynamicCommandPayload payload);

    @Override
    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin(owningPlugin);
    }

    private boolean checkPermissions(CommandSender target, String[] args) {
        Optional<DynamicSubcommand> subcommand = subcommandFromArgs(args);

        if (subcommand.isPresent()) {
            return testPermissionString(target,getPermission() + "." + subcommand.get().permission());
        } else {
            return testPermissionString(target, getPermission());
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // Check to see if help was requested
        if (Arrays.asList(HELP_ARGS).contains(args[0])) {
            sendHelp(sender);
            return true;
        }

        if (checkPermissions(sender, args)) {

            if (!argsInBounds(args)) {
                sender.sendMessage("§cNumber of arguments for this command was invalid");
                sendHelp(sender);
                return true;
            }

            if (isPlayerOnly() && !(sender instanceof Player)) {
                sender.sendMessage("§cThis command is intended to be sent by Players only");
                return true;
            }

            return doCommand(new DynamicCommandPayload(sender, commandLabel, args));

        } else {
            sender.sendMessage("§cYou do not have permission to access this command");
            return true;
        }
    }

    private boolean testPermissionString(CommandSender target, String permission) {
        // Firstly, determine if the sender is the Console or a CommandBlock
        if (target instanceof ConsoleCommandSender || target instanceof BlockCommandSender) {
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

    private Optional<DynamicSubcommand> subcommandFromArgs(String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(this.getClass().getAnnotationsByType(DynamicSubcommand.class))
                .filter(sub -> Arrays.stream(sub.aliases())
                        .anyMatch(al -> al.equals(args[0])))
                .findFirst();
    }

    private void sendHelp(CommandSender cs) {
        String[] help = this.getUsage().split("\\n");

        for (String str : help) {
            cs.sendMessage(str);
        }
    }
}
