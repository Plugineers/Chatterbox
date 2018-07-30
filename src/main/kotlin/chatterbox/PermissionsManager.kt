package chatterbox

import chatterbox.api.Messenger
import chatterbox.core.messengers.ConsoleMessenger
import chatterbox.core.messengers.PlayerMessenger
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit

private var ALL_WORLDS = "*"

object PermissionsManager {

    private var servicesChecked = false

    private var permissionHandler: Permission? = null
        get() {
            if (!servicesChecked) {
                field = Bukkit.getServer().servicesManager.getRegistration(Permission::class.java).provider
                servicesChecked = true
            }

            return field
        }

    init {
        println(if (permissionHandler != null) "Captured Vault permissions" else "Defaulting to Bukkit \"Superperms\"")
    }

    fun hasPermission(messenger: Messenger, permission: String, worldName: String = ALL_WORLDS): Boolean {
        if (messenger is ConsoleMessenger) {
            return true
        }

        val pMsgr: PlayerMessenger = messenger as PlayerMessenger

        return if (permissionHandler != null) {
            val perm = permissionHandler as Permission

            if (worldName == ALL_WORLDS) {
                return if (pMsgr.onlinePlayer != null) {
                    perm.playerHas(pMsgr.onlinePlayer, permission)
                } else {
                    if (pMsgr.offlinePlayer != null) {
                        perm.playerHas(null, pMsgr.offlinePlayer, permission)
                    } else {
                        false
                    }
                }
            } else {
                return if (pMsgr.offlinePlayer != null) {
                    perm.playerHas(worldName, pMsgr.offlinePlayer, permission)
                } else {
                    false
                }
            }
        } else {
            pMsgr.onlinePlayer?.hasPermission(permission) ?: false
        }
    }

    fun inGroup(playerMessenger: PlayerMessenger, groupName: String): Boolean {
        return if (permissionHandler != null) {
            val perm = permissionHandler as Permission

            return if (playerMessenger.onlinePlayer != null) {
                perm.playerInGroup(playerMessenger.onlinePlayer, groupName)
            } else {
                perm.playerInGroup(null, playerMessenger.offlinePlayer, groupName)
            }
        } else {
            hasPermission(playerMessenger, "group.$groupName")
        }
    }
}