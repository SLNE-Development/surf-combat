package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.CombatInstance
import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combo.events.PlayerComboEvent
import dev.slne.surf.combat.module.combo.events.PlayerComboExpiredEvent
import dev.slne.surf.surfapi.bukkit.api.extensions.pluginManager
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.bukkit.api.nms.bridges.packets.entity.nmsSpawnPackets
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableInt2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.random
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Display
import org.spongepowered.math.vector.Vector3f
import java.time.OffsetDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

data class ComboImpl(
    override val user: CombatUser,
    override val target: CombatUser,
    override var count: Int = 0,
    override var criticalCount: Int = 0,
    override var latest: OffsetDateTime = OffsetDateTime.now(),
) : Combo {
    private val _displayIds = mutableInt2ObjectMapOf<OffsetDateTime>()
    override val displayIds = _displayIds.freeze()

    override val isExpired: Boolean
        get() = OffsetDateTime.now().isAfter(latest.plus(ModuleCombo.COMBO_EXPIRY.toJavaDuration()))

    override fun increment(amount: Int, critical: Boolean) {
        count += amount

        if (critical) {
            criticalCount += 1
        }

        latest = OffsetDateTime.now()

        publish()
    }

    override fun expire() {
        publishExpiry()

        // Remove all active displays
        val iterator = _displayIds.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            val entityId = entry.key

            removeDisplay(entityId)

            iterator.remove()
        }

        sendComboExpiryDisplay()
    }

    override fun publish() {
        pluginManager.callEvent(PlayerComboEvent(this))
    }

    override fun publishExpiry() {
        pluginManager.callEvent(PlayerComboExpiredEvent(this))
    }

    override fun sendComboDisplay() = sendDisplay {
        warning("+")
        spacer(" - ")
        warning("$count")

        if (criticalCount > 0) {
            warning("($criticalCount)")
        }
    }

    fun sendComboExpiryDisplay() = sendAndRemoveDisplay(2.0, 3.seconds) {
        error("X")
        spacer(" - ")
        error("$count")

        if (criticalCount > 0) {
            error("($criticalCount)")
        }
    }

    @Suppress("UnstableApiUsage")
    @OptIn(NmsUseWithCaution::class)
    private fun sendDisplay(
        sizeModifier: Double = 1.0,
        expiry: Duration = 1.seconds,
        content: SurfComponentBuilder.() -> Unit
    ): Int {
        val entityId = random.nextInt()

        val bukkitPlayer = user.bukkitPlayer
            ?: throw IllegalStateException("User must be online to send combo display")

        val targetPlayer = target.bukkitPlayer
            ?: throw IllegalStateException("Target user must be online to send combo display")

        val playerLocation = bukkitPlayer.location.clone()
        val targetLocation = targetPlayer.location.clone()

        val direction = targetLocation.toVector().subtract(playerLocation.toVector()).normalize()
        val offsetDistance = 2.0
        val yOffset = 1.0

        val displayPosition = targetLocation.toVector().add(direction.multiply(offsetDistance))

        val location = Location(
            bukkitPlayer.world,
            displayPosition.x,
            displayPosition.y + yOffset,
            displayPosition.z
        )

        val spawnPacket = nmsSpawnPackets.spawnTextDisplay(
            entityId = entityId,
            position = location
        ) {
            val size = 0.5f * sizeModifier
            scale = Vector3f(size, size, size)
            billboardConstraints = Display.Billboard.VERTICAL
            text = SurfComponentBuilder.builder().apply(content).build()
        }

        spawnPacket.execute(bukkitPlayer)

        _displayIds[entityId] = OffsetDateTime.now().plus(expiry.toJavaDuration())
        
        return entityId
    }

    @OptIn(NmsUseWithCaution::class)
    private fun removeDisplay(entityId: Int) {
        val bukkitPlayer = user.bukkitPlayer
            ?: throw IllegalStateException("User must be online to remove combo display")

        nmsSpawnPackets.despawn(entityId).execute(bukkitPlayer)
    }

    override fun clearExpiredDisplays() {
        val now = OffsetDateTime.now()
        val iterator = _displayIds.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            val expirationTime = entry.value

            if (now.isAfter(expirationTime)) {
                removeDisplay(entry.key)

                iterator.remove()
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun sendAndRemoveDisplay(
        sizeModifier: Double = 1.0,
        expiry: Duration = 1.seconds,
        content: SurfComponentBuilder.() -> Unit
    ) = CombatInstance.launch {
        val entityId = sendDisplay(sizeModifier, expiry, content)
        delay(expiry)
        removeDisplay(entityId)
    }
}
