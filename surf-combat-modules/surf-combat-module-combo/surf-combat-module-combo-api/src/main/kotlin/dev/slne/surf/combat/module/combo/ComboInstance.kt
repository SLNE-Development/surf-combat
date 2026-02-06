package dev.slne.surf.combat.module.combo

import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import org.jetbrains.annotations.Unmodifiable

private val instance = requiredService<ComboInstance>()

interface ComboInstance {
    val combos: @Unmodifiable ObjectList<out Combo>

    fun clearExpiredCombos()
    fun findOrCreateCombo(user: CombatUser, target: CombatUser): Combo

    companion object : ComboInstance by instance {
        val INSTANCE get() = instance
    }
}