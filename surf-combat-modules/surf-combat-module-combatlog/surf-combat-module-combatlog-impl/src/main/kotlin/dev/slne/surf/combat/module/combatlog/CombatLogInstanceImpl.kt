package dev.slne.surf.combat.module.combatlog

import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.user.CombatUser

@AutoService(CombatLogInstance::class)
class CombatLogInstanceImpl : CombatLogInstance {
    override fun createCombatLog(user: CombatUser) = CombatLogImpl(user)
}