package chatterbox.api

interface MessagePayload {
    val messenger: Messenger

    var message: String
}