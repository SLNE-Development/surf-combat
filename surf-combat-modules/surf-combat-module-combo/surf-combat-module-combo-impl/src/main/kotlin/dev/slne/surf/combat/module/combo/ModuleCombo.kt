package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.module.CombatModule
import dev.slne.surf.combat.module.combo.jobs.PlayerComboJob
import dev.slne.surf.combat.module.combo.listeners.PlayerComboListener

object ModuleCombo : CombatModule(
    "combo",
    PlayerComboListener
) {
    override suspend fun onEnable() {
        PlayerComboJob.start()
    }

    override suspend fun onDisable() {
        PlayerComboJob.stop()
    }
}