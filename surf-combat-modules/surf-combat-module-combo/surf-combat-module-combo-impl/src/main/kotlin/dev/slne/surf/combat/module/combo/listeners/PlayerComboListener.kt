package dev.slne.surf.combat.module.combo.listeners

import dev.slne.surf.combat.api.user.combatUser
import dev.slne.surf.combat.module.combatlog.events.player.PlayerDamageEvent
import dev.slne.surf.combat.module.combo.ComboInstance
import dev.slne.surf.combat.module.combo.events.PlayerComboEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object PlayerComboListener : Listener {
    private const val VISUALIZE_AT = 3

    @EventHandler
    fun onPlayerDamage(event: PlayerDamageEvent) {
        val target = event.player
        val damager = event.damageEntity as? Player ?: return

        val damagerUser = damager.combatUser
        val targetUser = target.combatUser

        val lastEntityDamageEvent = event.lastEntityDamageEvent

        val isCriticalHit = if (lastEntityDamageEvent is EntityDamageByEntityEvent) {
            lastEntityDamageEvent.isCritical
        } else false

        ComboInstance.findOrCreateCombo(damagerUser, targetUser)
            .increment(critical = isCriticalHit)
    }

    @EventHandler
    fun onPlayerCombo(event: PlayerComboEvent) {
        if (event.combo.count <= VISUALIZE_AT) return

        event.combo.sendComboDisplay()
    }
}