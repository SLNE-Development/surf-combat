package dev.slne.surf.combat.module.combo.listeners

import dev.slne.surf.combat.api.user.combatUser
import dev.slne.surf.combat.module.combatlog.events.player.PlayerDamageEvent
import dev.slne.surf.combat.module.combo.ComboInstance
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object PlayerComboListener : Listener {
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
}