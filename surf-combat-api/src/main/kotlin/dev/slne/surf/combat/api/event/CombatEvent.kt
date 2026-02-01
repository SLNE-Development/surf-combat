package dev.slne.surf.combat.api.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class CombatEvent : Event() {
    override fun getHandlers() = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()
    }
}