package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.module.combo.events.PlayerComboEvent
import dev.slne.surf.combat.module.combo.events.PlayerComboExpiredEvent
import dev.slne.surf.surfapi.bukkit.api.extensions.pluginManager
import java.time.OffsetDateTime
import kotlin.time.toJavaDuration

data class ComboImpl(
    override val user: CombatUser,
    override val target: CombatUser,
    override var count: Int = 0,
    override var latest: OffsetDateTime = OffsetDateTime.now()
) : Combo {
    override val isExpired: Boolean
        get() = OffsetDateTime.now().isAfter(latest.plus(ModuleCombo.COMBO_EXPIRY.toJavaDuration()))

    override fun increment(amount: Int) {
        count += amount
        latest = OffsetDateTime.now()

        publish()
    }

    override fun publish() {
        pluginManager.callEvent(PlayerComboEvent(this))
    }

    override fun publishExpiry() {
        pluginManager.callEvent(PlayerComboExpiredEvent(this))
    }
}
