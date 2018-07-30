package chatterbox.core.messengers

import chatterbox.api.Messenger
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class PlayerMessenger constructor(private val uuid: UUID) : Messenger {
    override var focus: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

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