package dev.slne.surf.combat.module.combatlog

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.surfapi.core.api.util.requiredService

private val instance = requiredService<CombatLogInstance>()

interface CombatLogInstance {
    fun createCombatLog(user: CombatUser): CombatLog

    companion object : CombatLogInstance by instance {
        val INSTANCE get() = instance
    }
}