package dev.slne.surf.combat.module.combatlog

import dev.slne.surf.combat.api.user.CombatUser
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import org.bukkit.entity.Player
import org.jetbrains.annotations.Unmodifiable
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

interface CombatLog {
    val user: CombatUser
    val logs: @Unmodifiable Object2ObjectMap<UUID, OffsetDateTime>

    val latestCombat: OffsetDateTime?
    val remainingDuration: Duration?
    val isInCombat: Boolean

    fun handleLogout(player: Player)
    fun startCombatWith(target: CombatUser)
    fun removeInactiveLogs()
}