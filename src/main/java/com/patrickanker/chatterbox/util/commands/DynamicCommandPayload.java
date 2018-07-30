package com.patrickanker.chatterbox.util.commands;

import org.bukkit.command.CommandSender;

public class DynamicCommandPayload {
    private final CommandSender commandSender;
    private final String label;
    private final String[] args;

    public DynamicCommandPayload(CommandSender sender, String commandLabel, String[] arguments) {
        this.commandSender = sender;
        this.label = commandLabel;
        this.args = arguments;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArguments() {
        return args;
    }
}
