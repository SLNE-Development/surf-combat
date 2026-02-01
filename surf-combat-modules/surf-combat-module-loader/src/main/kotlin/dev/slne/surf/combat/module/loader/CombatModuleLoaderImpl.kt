package dev.slne.surf.combat.module.loader

import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.module.CombatModule
import dev.slne.surf.combat.api.module.CombatModuleLoader
import dev.slne.surf.combat.module.combatlog.ModuleCombatLog
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

@AutoService(CombatModuleLoader::class)
object CombatModuleLoaderImpl : CombatModuleLoader {
    private val log = logger()

    private val _availableModules = mutableObjectSetOf<CombatModule>()
    private val _loadedModules = mutableObjectSetOf<CombatModule>()
    private val _enabledModules = mutableObjectSetOf<CombatModule>()

    override val availableModules get() = _availableModules.freeze()
    override val loadedModules get() = _loadedModules.freeze()
    override val enabledModules get() = _enabledModules.freeze()

    private fun registerModules() {
        _availableModules.add(ModuleCombatLog)
    }

    override suspend fun onLoad() {
        registerModules()

        availableModules.forEach { module ->
            try {
                module.internalOnLoad()
                loadedModules.add(module)
            } catch (e: Exception) {
                log.atSevere()
                    .withCause(e)
                    .log("Failed to load module ${module.name}, skipping...")
                return@forEach
            }
        }
    }

    override suspend fun onEnable() {
        loadedModules.forEach { module ->
            try {
                module.internalOnEnable()
                enabledModules.add(module)
            } catch (e: Exception) {
                log.atSevere()
                    .withCause(e)
                    .log("Failed to enable module ${module.name}, skipping...")
                return@forEach
            }
        }
    }

    override suspend fun onDisable() {
        enabledModules.forEach { module ->
            try {
                module.internalOnDisable()
            } catch (e: Exception) {
                log.atSevere()
                    .withCause(e)
                    .log("Failed to disable module ${module.name}, skipping...")
                return@forEach
            }
        }
    }
}