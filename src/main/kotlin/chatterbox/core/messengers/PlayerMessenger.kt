package chatterbox.core.messengers

import chatterbox.api.Messenger
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class PlayerMessenger constructor(uid: UUID) : Messenger {

    private val uuid: UUID = uid

    override fun getFocus(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assignFocus(channelID: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val id: String
        get() = uuid.toString()

    var offlinePlayer: OfflinePlayer? = null
        get() = Bukkit.getOfflinePlayer(uuid)

    var onlinePlayer: Player? = null
        get() = offlinePlayer?.player
}