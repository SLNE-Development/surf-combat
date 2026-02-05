package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import java.time.OffsetDateTime

interface Combo {
    val user: CombatUser
    val target: CombatUser

    val count: Int
    val latest: OffsetDateTime

    val isExpired: Boolean

    fun increment(amount: Int = 1)

    fun publish()
    fun publishExpiry()
}