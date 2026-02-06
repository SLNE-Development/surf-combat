package dev.slne.surf.combat.module.combatlog.listeners

import dev.slne.surf.combat.api.user.combatUser
import dev.slne.surf.combat.module.combatlog.events.player.ExtendedPlayerDeathEvent
import dev.slne.surf.combat.module.combatlog.events.player.PlayerCombatStartEvent
import dev.slne.surf.combat.module.combatlog.events.player.PlayerDamageEvent
import dev.slne.surf.combat.module.combatlog.user.combatLog
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

object DamageListener : Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val lastEntityDamageEvent = player.lastDamageCause

        var damageEntity: Entity? = null
        var damageBlock: Block? = null

        if (lastEntityDamageEvent is EntityDamageByEntityEvent) {
            damageEntity = lastEntityDamageEvent.damager
        } else if (lastEntityDamageEvent is EntityDamageByBlockEvent) {
            damageBlock = lastEntityDamageEvent.damager
        }

        val extendedDamageEvent = PlayerDamageEvent(
            player = player,
            lastEntityDamageEvent = lastEntityDamageEvent,
            damageEntity = damageEntity,
            damageBlock = damageBlock
        )

        if (!extendedDamageEvent.callEvent()) {
            event.cancel()
            return
        }

        val damageeUser = player.combatUser
        val damagerUser = (damageEntity as? Player)?.combatUser ?: return

        val event = PlayerCombatStartEvent(
            player = damageeUser,
            target = damagerUser
        )

        if (event.callEvent()) {
            damageeUser.combatLog.startCombatWith(damagerUser)
            damagerUser.combatLog.startCombatWith(damageeUser)
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val lastEntityDamageEvent = player.lastDamageCause

        require(lastEntityDamageEvent != null) {
            "Player ${player.name} has no last damage cause on death!"
        }

        var killerEntity: Entity? = player.killer
        var killerBlock: Block? = null

        if (killerEntity == null) {
            if (lastEntityDamageEvent is EntityDamageByEntityEvent) {
                killerEntity = lastEntityDamageEvent.damager
            } else if (lastEntityDamageEvent is EntityDamageByBlockEvent) {
                killerBlock = lastEntityDamageEvent.damager
            }
        }

        val extendedDeathEvent = ExtendedPlayerDeathEvent(
            player = player,
            lastEntityDamageEvent = lastEntityDamageEvent,
            killerEntity = killerEntity,
            killerBlock = killerBlock,
            deathMessage = event.deathMessage()
        )

        if (!extendedDeathEvent.callEvent()) {
            event.cancel()
            return
        }

        event.deathMessage(extendedDeathEvent.deathMessage)
    }
}