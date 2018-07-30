package chatterbox.core

import chatterbox.api.Channel
import java.util.*
import kotlin.math.floor
import kotlin.reflect.KClass

private fun generateID(len: Int = 15): String {
    val sampleSpace = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val idBuilder   = StringBuilder()
    val rnd         = Random()

    while (idBuilder.length < len) {
        idBuilder.append(sampleSpace[floor(rnd.nextFloat() * sampleSpace.length) as Int])
    }

    return idBuilder.toString()
}

object ChannelManager {
    private val registeredChannels: MutableSet<Channel> = mutableSetOf()
    private val registeredTypes: MutableSet<KClass<Channel>> = mutableSetOf()

    fun hasChannel(channel: Channel): Boolean = registeredChannels.contains(channel)

    fun makeChannel(clazz: KClass<Channel>, options: Map<String, Any>): Channel {
        TODO("Yeah not done yet")
    }
}