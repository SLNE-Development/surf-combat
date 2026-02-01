package dev.slne.surf.combat.module.combatlog

import dev.slne.surf.combat.api.module.CombatModule
import dev.slne.surf.combat.module.combatlog.jobs.CombatLogJob
import dev.slne.surf.combat.module.combatlog.listeners.DamageListener
import dev.slne.surf.combat.module.combatlog.listeners.OnlineListener
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object ModuleCombatLog : CombatModule(
    name = "combatlog",
    events = objectSetOf(
        DamageListener,
        OnlineListener
    )
) {
    override suspend fun onEnable() {
        CombatLogJob.start()
    }

    override suspend fun onDisable() {
        CombatLogJob.stop()
    }
}