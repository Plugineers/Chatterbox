package chatterbox

import org.bukkit.plugin.Plugin;

val LOGGING_ID = "Chatterbox";

object Chatterbox {

    private var executingPlugin: String = ""

    fun start(plugin: Plugin) {
        executingPlugin = plugin.name
    }
}
