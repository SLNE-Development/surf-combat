package dev.slne.surf.combat.module.combatlog

import dev.slne.surf.combat.api.module.CombatModule
import dev.slne.surf.combat.module.combatlog.jobs.CombatLogJob
import dev.slne.surf.combat.module.combatlog.listeners.DamageListener
import dev.slne.surf.combat.module.combatlog.listeners.OnlineListener
import kotlin.time.Duration.Companion.seconds

object ModuleCombatLog : CombatModule(
    name = "combatlog",
    DamageListener,
    OnlineListener
) {
    val COMBAT_TIME = 11.seconds

    override suspend fun onEnable() {
        CombatLogJob.start()
    }

    override suspend fun onDisable() {
        CombatLogJob.stop()
    }
}