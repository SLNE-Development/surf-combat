package dev.slne.surf.combat.server.user

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.user.CombatUser
import dev.slne.surf.combat.api.user.CombatUserManager
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import java.util.*

@AutoService(CombatUserManager::class)
class CombatUserManagerImpl : CombatUserManager {
    private val userCache = Caffeine.newBuilder()
        .build<UUID, CombatUser> { uuid ->
            CombatUser(uuid)
        }

    override val users: @Unmodifiable ObjectSet<CombatUser>
        get() = userCache.asMap().values.toObjectSet()

    override fun getUser(uuid: UUID) = userCache.get(uuid)

    override fun invalidateUser(uuid: UUID) {
        userCache.invalidate(uuid)
    }
}