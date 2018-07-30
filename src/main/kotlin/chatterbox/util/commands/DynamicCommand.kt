package chatterbox.util.commands

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class DynamicCommand(
        val aliases: Array<String>,

        val bounds: IntArray,

        val help: String = "§cNo help provided for this command",

        val permission: String = "",

        val playerOnly: Boolean = false
)