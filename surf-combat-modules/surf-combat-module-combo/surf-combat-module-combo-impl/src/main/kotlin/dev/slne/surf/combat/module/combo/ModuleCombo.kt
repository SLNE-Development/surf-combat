package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.module.CombatModule
import dev.slne.surf.combat.module.combo.jobs.PlayerComboJob
import dev.slne.surf.combat.module.combo.listeners.PlayerComboListener
import kotlin.time.Duration.Companion.seconds

object ModuleCombo : CombatModule(
    "combo",
    PlayerComboListener
) {
    val COMBO_EXPIRY = 1.seconds

    override suspend fun onEnable() {
        PlayerComboJob.start()
    }

    override suspend fun onDisable() {
        PlayerComboJob.stop()
    }
}