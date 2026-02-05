package dev.slne.surf.combat.module.combatlog.user

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combatlog.CombatLog
import dev.slne.surf.combat.module.combatlog.CombatLogInstance


private const val CACHE_KEY_COMBAT_LOG = "combatlog"
var CombatUser.combatLog: CombatLog
    get() = getCachedValue(CACHE_KEY_COMBAT_LOG) ?: CombatLogInstance.createCombatLog(this).also {
        setCachedValue(CACHE_KEY_COMBAT_LOG, it)
    }
    set(value) {
        setCachedValue(CACHE_KEY_COMBAT_LOG, value)
    }