package dev.slne.surf.combat.module.combatlog.events.player

import dev.slne.surf.combat.api.event.CombatEvent
import dev.slne.surf.combat.api.user.CombatUser

class PlayerCombatEndEvent(
    val player: CombatUser,
    val target: CombatUser
) : CombatEvent()