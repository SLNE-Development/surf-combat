package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CombatEvent
import dev.slne.surf.combat.api.user.CombatUser
import org.bukkit.event.HandlerList

class PlayerCombatEndEvent(
    val player: CombatUser,
    val target: CombatUser
) : CombatEvent() {
    override fun getHandlers() = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLER_LIST
    }
}