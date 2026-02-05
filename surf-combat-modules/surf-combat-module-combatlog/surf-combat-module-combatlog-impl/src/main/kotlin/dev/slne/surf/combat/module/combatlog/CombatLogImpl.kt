package dev.slne.surf.combat.module.combatlog

import com.github.shynixn.mccoroutine.folia.ticks
import dev.slne.surf.combat.api.CombatInstance
import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combatlog.ModuleCombatLog.COMBAT_TIME
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.toJavaDuration

class CombatLogImpl(
    override val user: CombatUser
) : CombatLog {
    private val _logs = mutableObject2ObjectMapOf<UUID, OffsetDateTime>()
    override val logs get() = _logs.freeze()

    override val latestCombat: OffsetDateTime?
        get() = logs.values.maxOrNull()

    override val remainingDuration: Duration?
        get() = latestCombat?.let { combatTime ->
            val now = OffsetDateTime.now()
            val duration = Duration.between(now, combatTime).abs()
            val remaining = COMBAT_TIME.toJavaDuration() - duration

            if (remaining.isNegative) {
                null
            } else {
                remaining
            }
        }

    override val isInCombat get() = logs.isNotEmpty()

    override fun handleLogout(player: Player) {
        if (!isInCombat) {
            return
        }

        CombatInstance.launch {
            val location = player.location

            val armorContents = player.inventory.armorContents
            val storageContents = player.inventory.storageContents
            val extraContents = player.inventory.extraContents

            val contents = mutableObjectListOf<ItemStack>().apply {
                addAll(armorContents.filterNotNull().filterNot { it.type == Material.AIR })
                addAll(storageContents.filterNotNull().filterNot { it.type == Material.AIR })
                addAll(extraContents.filterNotNull().filterNot { it.type == Material.AIR })
            }

            val experience = player.calculateTotalExperiencePoints()

            player.inventory.clear()
            player.level = 0
            player.exp = 0F

            delay(1.ticks)

            withContext(CombatInstance.regionDispatcher(location)) {
                contents.forEach { item ->
                    location.world.dropItemNaturally(location, item)
                }

                location.world.spawnEntity(
                    location,
                    EntityType.EXPERIENCE_ORB,
                    CreatureSpawnEvent.SpawnReason.CUSTOM
                ) { entity ->
                    require(entity is ExperienceOrb) { "Expected ExperienceOrb, got ${entity.type}" }

                    entity.experience = experience
                }
            }
        }
    }

    override fun startCombatWith(target: CombatUser) {
        _logs[target.uuid] = OffsetDateTime.now()
    }

    override fun removeInactiveLogs() {
        val now = OffsetDateTime.now()

        val inactiveTargets = logs.filter { (target, combatTime) ->
            val expired = Duration.between(now, combatTime).abs() > COMBAT_TIME.toJavaDuration()
            val targetLeft = Bukkit.getPlayer(target) == null

            expired || targetLeft
        }.keys

        inactiveTargets.forEach { target ->
            _logs.remove(target)
        }
    }

    override fun toString(): String {
        return "CombatLogImpl(logs=$logs, latestCombat=$latestCombat, remainingDuration=$remainingDuration, isInCombat=$isInCombat)"
    }
}