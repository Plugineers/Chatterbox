package chatterbox

import chatterbox.api.Messenger
import chatterbox.util.commands.DynamicCommandRegistry
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.logging.Level

const val LOGGING_ID: String = "Chatterbox"

object Chatterbox {
    private val knownMessengers: MutableSet<Messenger> = mutableSetOf()
    private val commandRegistry by lazy {
        DynamicCommandRegistry(executingPlugin)
    }

    private var executingPlugin: String = ""

    fun start(plugin: Plugin) {
        executingPlugin = plugin.name

        // Configuration loading here

        // Command registration here
    }

    fun hasMessenger(id: String): Boolean = knownMessengers.firstOrNull { it.id == id } != null

    fun findMessenger(id: String): Messenger? = knownMessengers.firstOrNull { it.id == id }

    fun addMessenger(messenger: Messenger) {
        if (!hasMessenger(messenger.id)) {
            knownMessengers.add(messenger)
        }
    }

    fun removeMessenger(id: String) {
        if (hasMessenger(id)) {
            knownMessengers.remove(findMessenger(id)!!) // !! Check is valid from hasMessenger call
        }
    }
}

fun logInfo(message: String)   = Bukkit.getLogger().log(Level.INFO, "[$LOGGING_ID] $message")
fun logWarn(message: String)   = Bukkit.getLogger().log(Level.WARNING, "[$LOGGING_ID] $message")
fun logSevere(message: String) = Bukkit.getLogger().log(Level.SEVERE, "[$LOGGING_ID] $message")
