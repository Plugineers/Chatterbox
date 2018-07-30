package chatterbox.util.commands

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class Description(val value: String = "")