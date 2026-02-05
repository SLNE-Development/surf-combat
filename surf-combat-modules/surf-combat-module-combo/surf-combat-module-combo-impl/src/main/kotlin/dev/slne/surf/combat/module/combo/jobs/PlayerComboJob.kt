package dev.slne.surf.combat.module.combo.jobs

import dev.slne.surf.combat.api.job.CombatJob
import dev.slne.surf.combat.module.combo.ComboInstance
import dev.slne.surf.combat.module.combo.ModuleCombo

object PlayerComboJob : CombatJob(ModuleCombo.moduleScope, ModuleCombo.COMBO_EXPIRY) {
    override fun tick() {
        ComboInstance.clearExpiredCombos()
    }
}