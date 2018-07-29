package chatterbox.api

interface Messenger {

    val id: String

    fun getFocus(): String

    fun assignFocus(channelID: String)
}