package chatterbox.util.commands

import chatterbox.logInfo
import chatterbox.logSevere
import chatterbox.logWarn
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import java.lang.reflect.Field

val HELP_ARGS = arrayOf("help", "h", "?")

class DynamicCommandRegistry(private val owningPlugin: String) {
    private val registeredCommands = mutableSetOf<DynamicPluginCommand>()

    private fun hasRegisteredCommand(dynamicCommand: DynamicCommand) = registeredCommands.any { it.name == dynamicCommand.aliases[0] }

    fun <T : DynamicPluginCommand> registerCommand(cmd: () -> T): DynamicCommandRegistry {
        val t = cmd()

        if (t::class.annotations.any { it is DynamicCommand }) {
            val registration: DynamicCommand = t::class.annotations.first { it is DynamicCommand } as DynamicCommand
            val registrationName             = registration.aliases[0]

            if (hasRegisteredCommand(registration)) {
                logWarn("Attempted to doubly register command \"$registrationName\"")
                return this
            }

            t.owningPlugin = owningPlugin
            t.name         = registrationName
            t.label        = registrationName
            t.aliases      = registration.aliases.copyOfRange(1, registration.aliases.size - 1).asList()
            t.argbounds    = registration.bounds

            t.usage        = registration.help
            t.playerOnly   = registration.playerOnly

            if (t::class.annotations.any { it is Description }) {
                val desc: Description = t::class.annotations.first { it is Description } as Description
                t.description         = desc.value
            }

            fun commandFromMap(commandMap: CommandMap, commandName: String): Command? = commandMap.getCommand(commandName)

            try {
                val commandMapField: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
                commandMapField.isAccessible = true

                val commandMap: CommandMap = commandMapField.get(Bukkit.getServer()) as CommandMap
                val cmdFromMap = commandFromMap(commandMap, registrationName)

                if (cmdFromMap != null) {
                    logWarn("Did not register \"$registrationName\" due to already existing command")
                } else {
                    logInfo("Successfully registered \"$registrationName\"")
                    commandMap.register(t.label, t)
                    registeredCommands.add(t)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return this
            }

        } else {
            logSevere("Invalid registration for command class encountered: \"${t::class.simpleName}\"")
        }

        return this
    }
}