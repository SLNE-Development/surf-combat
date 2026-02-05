package dev.slne.surf.combat.server.listeners

import dev.slne.surf.combat.api.user.CombatUserManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object OnlineListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        CombatUserManager.invalidateUser(event.player.uniqueId)
    }
}