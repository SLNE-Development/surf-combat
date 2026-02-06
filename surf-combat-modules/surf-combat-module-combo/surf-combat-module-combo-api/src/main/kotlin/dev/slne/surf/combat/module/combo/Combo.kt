package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import java.time.OffsetDateTime
import kotlin.time.Duration.Companion.seconds

interface Combo {
    val user: CombatUser
    val target: CombatUser

    val count: Int
    val criticalCount: Int

    val expiry: OffsetDateTime
    val isExpired: Boolean

    fun increment(amount: Int = 1, critical: Boolean = false)

    fun expire()
    fun publish()
    fun publishExpiry()

    fun sendComboDisplay()
    fun sendComboExpiryDisplay()

    companion object {
        val COMBO_EXPIRY = 5.seconds
        const val COMBO_SHOWCASE_AFTER = 3
    }
}