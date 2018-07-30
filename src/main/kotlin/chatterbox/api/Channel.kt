package chatterbox.api

interface Channel {

    fun addMessenger(messenger: Messenger): Boolean

    fun removeMessenger(messenger: Messenger): Boolean

    fun hasMessenger(messenger: Messenger): Boolean

    fun currentMessengers(): List<Messenger>

    fun makePayload(messenger: Messenger, message: String): MessagePayload

    fun allowPayload(payload: MessagePayload): Boolean

    fun dispatchMessage(payload: MessagePayload): Boolean

    val permissionRoot: String

    var focusFormat: String

    var passiveFormat: String

    var consoleFormat: String
}