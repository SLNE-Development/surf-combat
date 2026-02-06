package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import org.jetbrains.annotations.Unmodifiable
import java.time.OffsetDateTime

interface Combo {
    val user: CombatUser
    val target: CombatUser

    val count: Int
    val criticalCount: Int

    val latest: OffsetDateTime
    val isExpired: Boolean

    val displayIds: @Unmodifiable Int2ObjectMap<OffsetDateTime>

    fun increment(amount: Int = 1, critical: Boolean = false)

    fun expire()
    fun publish()
    fun publishExpiry()

    fun sendComboDisplay(): Int
    fun clearExpiredDisplays()
}