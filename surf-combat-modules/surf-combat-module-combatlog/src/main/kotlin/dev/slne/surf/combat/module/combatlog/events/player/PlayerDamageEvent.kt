package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CancellableCombatEvent
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class PlayerDamageEvent(
    val player: Player,
    val lastEntityDamageEvent: EntityDamageEvent?,
    val damageEntity: Entity?,
    val damageBlock: Block?,
) : CancellableCombatEvent()