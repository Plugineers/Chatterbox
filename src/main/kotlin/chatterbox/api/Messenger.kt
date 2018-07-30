package chatterbox.api

interface Messenger {

    val id: String

    var focus: String

    fun assignFocus(channelID: String)
}