package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CancellableCombatEvent
import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent

class ExtendedPlayerDeathEvent(
    val player: Player,
    val lastEntityDamageEvent: EntityDamageEvent?,
    val killerEntity: Entity?,
    val killerBlock: Block?,
    var deathMessage: Component?
) : CancellableCombatEvent() {
    override fun getHandlers() = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLER_LIST
    }
}