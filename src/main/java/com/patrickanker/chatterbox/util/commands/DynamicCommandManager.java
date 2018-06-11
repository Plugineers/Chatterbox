package com.patrickanker.chatterbox.util.commands;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

import com.patrickanker.chatterbox.Chatterbox;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

public final class DynamicCommandManager {
    private final String[] helpArgs = {"help", "h", "?"};
    private final LinkedList<DynamicPluginCommand> registeredCommands = new LinkedList<>();
//    protected final Map<String, Method> aliases   = new HashMap<>();
//    protected final Map<Method, Object> instances = new HashMap<>();

    private final String owningPlugin;

    public DynamicCommandManager(String pl) {
        this.owningPlugin = pl;
    }

    private Plugin owningPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin(owningPlugin);
    }

    private boolean hasRegisteredCommand(DynamicCommand dynamicCommand) {
        return registeredCommands.stream()
                .anyMatch(cmd -> cmd.getName().equals(dynamicCommand.aliases()[0]));
    }

    public <T extends DynamicPluginCommand> DynamicCommandManager registerCommand(Supplier<T> cmd) {
        if (cmd.get().getClass().isAnnotationPresent(DynamicCommand.class)) {
            T t = cmd.get();
            DynamicCommand reg = t.getClass().getAnnotation(DynamicCommand.class);
            String regName     = reg.aliases()[0];

            if (hasRegisteredCommand(reg)) {
                Chatterbox.logWarn("Attempted to doubly register command \"" + regName + "\"");
                return this;
            }

            t.setOwningPlugin(owningPlugin);
            t.setName(regName);
            t.setLabel(regName);
            t.setAliases(Arrays.asList(Arrays.copyOfRange(reg.aliases(), 1, reg.aliases().length)));
            t.setArgumentBounds(reg.bounds());

            t.setUsage(reg.help());
            t.setPlayerOnly(reg.playerOnly());

            if (t.getClass().isAnnotationPresent(Description.class)) {
                Description desc = t.getClass().getAnnotation(Description.class);
                t.setDescription(desc.value());
            }

            try {
                final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);

                final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer()); // Force immutability for security

                // Verify if the command has not been already registered. If it has, move on.
                // TODO: Implement command overrider?
                if (getCommandFromMap(commandMap, regName).isPresent()) {
                    Command knownCommand = getCommandFromMap(commandMap, regName).get();

                    if (knownCommand instanceof PluginIdentifiableCommand) {
                        Chatterbox.logWarn("Did not register \"" + regName + "\" because of already existing command from \"" + ((PluginIdentifiableCommand) knownCommand).getPlugin().getName() + "\"");
                    } else {
                        Chatterbox.logWarn("Did not register \"" + regName + "\" because of already existing command from unknown or default source");
                    }
                } else {
                    Chatterbox.logInfo("Successfully registered command \"" + t.getName() + "\"");
                    commandMap.register(t.getLabel(), t);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return this;

        } else {
            Chatterbox.logSevere("Invalid registration for command class encountered: \"" + cmd.get().getClass().getSimpleName() + "\"");
            return this;
        }
    }

    private Optional<Command> getCommandFromMap(CommandMap cmap, String commandName) {
        return Optional.ofNullable(cmap.getCommand(commandName));
    }
}

