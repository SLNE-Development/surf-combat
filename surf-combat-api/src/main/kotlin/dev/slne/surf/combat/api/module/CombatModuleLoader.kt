package dev.slne.surf.combat.api.module

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import kotlin.reflect.KClass

private val moduleLoader = requiredService<CombatModuleLoader>()

interface CombatModuleLoader {
    val availableModules: @Unmodifiable ObjectSet<CombatModule>
    val loadedModules: @Unmodifiable ObjectSet<CombatModule>
    val enabledModules: @Unmodifiable ObjectSet<CombatModule>
    
    suspend fun onLoad()
    suspend fun onEnable()
    suspend fun onDisable()

    companion object : CombatModuleLoader by moduleLoader {
        val INSTANCE get() = moduleLoader

        fun getModule(clazz: KClass<out CombatModule>): CombatModule =
            enabledModules.first { it::class == clazz }

        inline fun <reified T : CombatModule> getModule(): T =
            getModule(T::class) as T
    }
}