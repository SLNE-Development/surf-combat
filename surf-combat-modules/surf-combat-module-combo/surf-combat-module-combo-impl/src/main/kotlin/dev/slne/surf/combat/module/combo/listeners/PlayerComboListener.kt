package dev.slne.surf.combat.module.combo.listeners

import dev.slne.surf.combat.api.user.combatUser
import dev.slne.surf.combat.module.combatlog.events.player.PlayerDamageEvent
import dev.slne.surf.combat.module.combo.ComboInstance
import dev.slne.surf.combat.module.combo.events.PlayerComboEvent
import dev.slne.surf.combat.module.combo.events.PlayerComboExpiredEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PlayerComboListener : Listener {
    @EventHandler
    fun onPlayerDamage(event: PlayerDamageEvent) {
        val target = event.player
        val damager = event.damageEntity as? Player ?: return

        val damagerUser = damager.combatUser
        val targetUser = target.combatUser

        ComboInstance.findOrCreateCombo(damagerUser, targetUser).increment()
    }

    @EventHandler
    fun onPlayerCombo(event: PlayerComboEvent) {
        val user = event.user
        val target = event.target
        val comboCount = event.count

        if (comboCount < 3) return

        user.bukkitPlayer?.sendText {
            info("Du hast eine ")
            variableValue(comboCount)
            info("-Combo auf ")
            variableValue(target.bukkitPlayer?.name ?: "Unbekannt")
            info("!")
        }

        target.bukkitPlayer?.sendText {
            variableValue(user.bukkitPlayer?.name ?: "Unbekannt")
            info(" hat eine ")
            variableValue(comboCount)
            info("-Combo auf dich!")
        }
    }

    @EventHandler
    fun onPlayerComboExpiry(event: PlayerComboExpiredEvent) {
        val user = event.user
        val target = event.target
        val comboCount = event.count

        if (comboCount < 3) return

        user.bukkitPlayer?.sendText {
            info("Deine ")
            variableValue(comboCount)
            info("-Combo auf ")
            variableValue(target.bukkitPlayer?.name ?: "Unbekannt")
            info(" ist abgelaufen!")
        }

        target.bukkitPlayer?.sendText {
            info("Die ")
            variableValue(comboCount)
            info("-Combo von ")
            variableValue(user.bukkitPlayer?.name ?: "Unbekannt")
            info(" auf dich ist abgelaufen!")
        }
    }
}