package dev.slne.surf.combat.module.combatlog.listeners

import dev.slne.surf.combat.api.user.combatUser
import dev.slne.surf.combat.module.combatlog.user.combatLog
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object OnlineListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.combatUser

        if (!user.combatLog.isInCombat) {
            return
        }

        user.combatLog.handleLogout(player)
    }
}