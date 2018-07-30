package chatterbox.util.commands

import chatterbox.Chatterbox
import chatterbox.PermissionsManager
import chatterbox.logSevere
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

private val HELP_ARGS: Array<String> = arrayOf("help", "h", "?")

data class DynamicCommandPayload(val sender: CommandSender, val commandLabel: String, val arguments: List<String>)

abstract class DynamicPluginCommand : Command, PluginIdentifiableCommand {
    private val commandName: String

    constructor(name: String) : super("", "", "", arrayListOf<String>()) {
        commandName = name
    }

    protected var owningPlugin = ""
    protected var argbounds    = intArrayOf(-1, -1)
    protected var playerOnly   = false

    fun argsInBounds(args: List<String>): Boolean {
        return if (argbounds[0] > 0 && args.size < argbounds[0]) {
            false
        } else {
            !(argbounds[1] > 0 && args.size > argbounds[1])
        }
    }

    override fun getPlugin(): Plugin = Bukkit.getPluginManager().getPlugin(owningPlugin)

    abstract fun doCommand(payload: DynamicCommandPayload): Boolean

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val argsList = args.asList()

        return if (HELP_ARGS.contains(args[0])) {
            sendHelp(sender); true
        } else {
            return if (checkPermissions(sender, argsList)) {
                return if (!argsInBounds(argsList)) {
                    sender.sendMessage("§cNumber of arguments for this command was incorrect")
                    sendHelp(sender)
                    true
                } else if (playerOnly && !(sender is Player)) {
                    sender.sendMessage("§cThis command is intended to be sent by players only")
                    true
                } else {
                    doCommand(DynamicCommandPayload(sender, commandLabel, argsList))
                }
            } else {
                sender.sendMessage("§cYou do not have permission to access this command")
                true
            }
        }
    }

    private fun checkPermissions(target: CommandSender, args: List<String>): Boolean {
        var sub = subcommandFromArgs(args)

        return if (sub != null) testPermissionString(target, "$permission.${sub.permission}") else testPermissionString(target, permission)
    }

    private fun subcommandFromArgs(args: List<String>): DynamicSubcommand? {
        return if (args.isEmpty()) {
            null
        } else {
            this::class.annotations.firstOrNull { it is DynamicSubcommand && it.aliases.contains(args[0].decapitalize()) } as DynamicSubcommand
        }
    }

    private fun sendHelp(commandSender: CommandSender) {
        val help = usage.split("\\n")

        for (helpstr in help) {
            commandSender.sendMessage(helpstr)
        }
    }
}

private fun testPermissionString(target: CommandSender, permission: String): Boolean {
    return if (target is ConsoleCommandSender || target is BlockCommandSender) true else {
        return if (permission == "") true else {
            val pMsgr = Chatterbox.findMessenger((target as Player).uniqueId.toString())

            return if (pMsgr == null) {
                logSevere("Unexpected missing PlayerMessenger instance from Player \"${target.name}\"")
                false
            } else {
                return if (PermissionsManager.hasPermission(pMsgr, permission)) true else {
                    PermissionsManager.hasPermission(pMsgr, permission, target.world.name)
                }
            }
        }
    }
}

