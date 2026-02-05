package dev.slne.surf.combat.module.combo

import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf

@AutoService(ComboInstance::class)
class ComboInstanceImpl : ComboInstance {
    private val _combos = mutableObjectListOf<ComboImpl>()
    override val combos get() = _combos.freeze()

    override fun findOrCreateCombo(user: CombatUser, target: CombatUser): Combo {
        val combo = combos.firstOrNull { it.user == user && it.target == target }

        if (combo != null) {
            return combo
        }

        val createdCombo = ComboImpl(user, target)
        _combos.add(createdCombo)

        return createdCombo
    }

    override fun clearExpiredCombos() {
        val iterator = _combos.iterator()

        while (iterator.hasNext()) {
            val combo = iterator.next()

            if (combo.isExpired) {
                combo.publishExpiry()
                iterator.remove()
            }
        }
    }
}