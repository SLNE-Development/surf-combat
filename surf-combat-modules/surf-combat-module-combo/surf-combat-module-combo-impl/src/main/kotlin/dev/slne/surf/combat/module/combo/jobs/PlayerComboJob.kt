package dev.slne.surf.combat.module.combo.jobs

import dev.slne.surf.combat.api.job.CombatJob
import dev.slne.surf.combat.module.combo.ComboInstance
import dev.slne.surf.combat.module.combo.ModuleCombo
import kotlin.time.Duration.Companion.seconds

object PlayerComboJob : CombatJob(ModuleCombo.moduleScope, 1.seconds) {
    override fun tick() {
        ComboInstance.clearExpiredCombos()
    }
}