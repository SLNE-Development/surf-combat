package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combo.Combo.Companion.COMBO_EXPIRY
import dev.slne.surf.combat.module.combo.Combo.Companion.COMBO_SHOWCASE_AFTER
import dev.slne.surf.combat.module.combo.events.PlayerComboEvent
import dev.slne.surf.combat.module.combo.events.PlayerComboExpiredEvent
import dev.slne.surf.surfapi.bukkit.api.extensions.pluginManager
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.showTitle
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.time.OffsetDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import org.bukkit.Sound as BukkitSound

data class ComboImpl(
    override val user: CombatUser,
    override val target: CombatUser,
    override var count: Int = 0,
    override var criticalCount: Int = 0,
    override var expiry: OffsetDateTime = OffsetDateTime.now().plus(COMBO_EXPIRY.toJavaDuration()),
) : Combo {
    override val isExpired: Boolean
        get() = OffsetDateTime.now().isAfter(expiry)

    override fun increment(amount: Int, critical: Boolean) {
        count += amount

        if (critical) {
            criticalCount += 1
        }

        expiry = OffsetDateTime.now().plus(COMBO_EXPIRY.toJavaDuration())

        publish()

        if (count >= COMBO_SHOWCASE_AFTER) {
            sendComboDisplay()
        }
    }

    override fun expire() {
        publishExpiry()
        sendComboExpiryDisplay()
    }

    override fun publish() {
        pluginManager.callEvent(PlayerComboEvent(this))
    }

    override fun publishExpiry() {
        pluginManager.callEvent(PlayerComboExpiredEvent(this))
    }

    private fun sendComboTitle(
        color: TextColor,
        stay: Duration,
    ) {
        val player = user.bukkitPlayer ?: return

        player.clearTitle()

        player.showTitle {
            title = Component.empty()
            subtitle {
                text("💢", color)
                spacer(" - ")
                text("$count", color)
                if (criticalCount > 0) {
                    appendSpace()
                    text("(🗡 $criticalCount)", color)
                }
            }
            times {
                fadeIn(0)
                stay(stay)
                fadeOut(0)
            }
        }
    }

    override fun sendComboDisplay() {
        sendComboTitle(Colors.INFO, COMBO_EXPIRY.plus(1.seconds))

        user.bukkitPlayer?.playSound {
            type(BukkitSound.ENTITY_EXPERIENCE_ORB_PICKUP)
            volume(.25f)
            source(Sound.Source.PLAYER)
        }
    }

    override fun sendComboExpiryDisplay() {
        sendComboTitle(Colors.ERROR, COMBO_EXPIRY)

        user.bukkitPlayer?.playSound {
            type(BukkitSound.ENTITY_VILLAGER_HURT)
            volume(.5f)
            source(Sound.Source.PLAYER)
        }
    }
}
