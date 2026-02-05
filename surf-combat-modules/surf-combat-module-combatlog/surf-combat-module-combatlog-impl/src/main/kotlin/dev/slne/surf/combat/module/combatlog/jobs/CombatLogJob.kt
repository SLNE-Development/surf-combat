package dev.slne.surf.combat.module.combatlog.jobs

import dev.slne.surf.combat.api.job.CombatJob
import dev.slne.surf.combat.api.user.CombatUserManager
import dev.slne.surf.combat.module.combatlog.ModuleCombatLog
import dev.slne.surf.combat.module.combatlog.user.combatLog
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlin.time.Duration.Companion.seconds

object CombatLogJob : CombatJob(
    scope = ModuleCombatLog.moduleScope,
    sleepDuration = 1.seconds
) {
    override fun tick() {
        CombatUserManager.users.forEach { user ->
            val combatLog = user.combatLog

            combatLog.removeInactiveLogs()
            val remainingSeconds = combatLog.remainingDuration ?: return@forEach

            user.bukkitPlayer?.sendActionBar(buildText {
                variableValue(remainingSeconds.toSeconds())
            })
        }
    }
}