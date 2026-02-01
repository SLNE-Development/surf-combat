package dev.slne.surf.combat.api.user

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import java.util.*

private val userManager = requiredService<CombatUserManager>()

interface CombatUserManager {
    val users: @Unmodifiable ObjectSet<CombatUser>

    fun getUser(uuid: UUID): CombatUser
    fun invalidateUser(uuid: UUID)

    companion object : CombatUserManager by userManager {
        val INSTANCE get() = userManager
    }
}