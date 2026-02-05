package dev.slne.surf.combat.module.combo.events

import dev.slne.surf.combat.api.event.CombatEvent
import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combo.Combo
import org.bukkit.event.HandlerList
import java.time.OffsetDateTime

class PlayerComboExpiredEvent(val combo: Combo) : CombatEvent() {
    val user: CombatUser by combo::user
    val target: CombatUser by combo::target
    val count: Int by combo::count
    val latest: OffsetDateTime by combo::latest

    override fun getHandlers() = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLER_LIST
    }
}