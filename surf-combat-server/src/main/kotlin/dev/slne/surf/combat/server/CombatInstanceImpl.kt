package dev.slne.surf.combat.server

import com.github.shynixn.mccoroutine.folia.scope
import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.CombatInstance
import dev.slne.surf.combat.module.loader.CombatModuleLoaderImpl
import dev.slne.surf.combat.server.listeners.OnlineListener
import dev.slne.surf.surfapi.bukkit.api.event.register

@AutoService(CombatInstance::class)
class CombatInstanceImpl : CombatInstance {
    override val pluginScope = plugin.scope
    override val pluginManager = plugin.server.pluginManager
    override val pluginInstance = plugin

    private val moduleLoader = CombatModuleLoaderImpl

    override suspend fun onLoad() {
        moduleLoader.onLoad()

        OnlineListener.register()
    }

    override suspend fun onEnable() {
        moduleLoader.onEnable()
    }

    override suspend fun onDisable() {
        moduleLoader.onDisable()
    }
}