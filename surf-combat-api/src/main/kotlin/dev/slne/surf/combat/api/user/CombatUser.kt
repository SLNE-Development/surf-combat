package dev.slne.surf.combat.api.user

import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

val Player.combatUser: CombatUser
    get() = CombatUser[this.uniqueId]

data class CombatUser(
    val uuid: UUID
) {
    private val cache = mutableObject2ObjectMapOf<String, Any?>()

    val bukkitPlayer get() = Bukkit.getPlayer(uuid)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getCachedValue(key: String): T? = cache[key] as T?

    fun <T : Any> setCachedValue(key: String, value: T?) {
        cache[key] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CombatUser

        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun toString(): String {
        return "CombatUser(cache=$cache, uuid=$uuid)"
    }

    companion object {
        operator fun get(uuid: UUID) = CombatUserManager.getUser(uuid)
    }
}
