package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CancellableCombatEvent
import dev.slne.surf.combat.api.user.CombatUser

class PlayerCombatStartEvent(
    val player: CombatUser,
    val target: CombatUser
) : CancellableCombatEvent()