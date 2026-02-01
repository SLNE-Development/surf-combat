package dev.slne.surf.combat.module.combatlog.user

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.time.Duration.Companion.seconds

val COMBAT_TIME = 10.seconds

data class CombatLog(
    val user: CombatUser,
    val logs: Object2ObjectMap<CombatUser, OffsetDateTime> = mutableObject2ObjectMapOf()
) {
    val latestCombat: OffsetDateTime?
        get() = logs.values.maxOrNull()

    val remainingSeconds: Long
        get() {
            val latest = latestCombat ?: return 0L
            val now = OffsetDateTime.now()
            val duration = Duration.between(now, latest).abs()
            val elapsedSeconds = duration.toSeconds()
            val remaining = COMBAT_TIME.inWholeSeconds - elapsedSeconds
            return remaining.coerceAtLeast(0L)
        }

    val isInCombat get() = logs.isNotEmpty()

    fun handleLogout(player: Player) {
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

    fun startCombatWith(other: CombatUser) {
        val now = OffsetDateTime.now()

        logs[other] = now
    }

    fun removeInactiveLogs() {
        val now = OffsetDateTime.now()

        val iterator = logs.object2ObjectEntrySet().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val lastCombatTime = entry.value
            val duration = Duration.between(now, lastCombatTime)

            if (duration.toSeconds() >= COMBAT_TIME.inWholeSeconds) {
                iterator.remove()
            }
        }
    }
}

private const val CACHE_KEY_COMBAT_LOG = "combatlog"
var CombatUser.combatLog: CombatLog
    get() = getCachedValue(CACHE_KEY_COMBAT_LOG) ?: CombatLog(this).also {
        setCachedValue(CACHE_KEY_COMBAT_LOG, it)
    }
    set(value) {
        setCachedValue(CACHE_KEY_COMBAT_LOG, value)
    }