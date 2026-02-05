package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import java.time.OffsetDateTime

interface Combo {
    val user: CombatUser
    val target: CombatUser

    val count: Int
    val criticalCount: Int

    val latest: OffsetDateTime
    val isExpired: Boolean

    fun increment(amount: Int = 1, critical: Boolean = false)

    fun publish()
    fun publishExpiry()
}