package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CancellableCombatEvent
import dev.slne.surf.combat.api.user.CombatUser
import org.bukkit.event.HandlerList

class PlayerCombatStartEvent(
    val player: CombatUser,
    val target: CombatUser
) : CancellableCombatEvent() {
    override fun getHandlers() = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLER_LIST
    }
}