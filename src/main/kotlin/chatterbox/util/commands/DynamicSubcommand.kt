package chatterbox.util.commands

@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class DynamicSubcommand(val aliases: Array<String>, val permission: String = "")