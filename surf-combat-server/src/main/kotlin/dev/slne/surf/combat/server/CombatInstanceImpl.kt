package dev.slne.surf.combat.server

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.regionDispatcher
import com.github.shynixn.mccoroutine.folia.scope
import com.google.auto.service.AutoService
import dev.slne.surf.combat.api.CombatInstance
import dev.slne.surf.combat.module.loader.CombatModuleLoaderImpl
import dev.slne.surf.combat.server.listeners.OnlineListener
import dev.slne.surf.surfapi.bukkit.api.event.register
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import org.bukkit.Location
import org.bukkit.entity.Entity
import kotlin.coroutines.CoroutineContext

@AutoService(CombatInstance::class)
class CombatInstanceImpl : CombatInstance {
    override val pluginScope by lazy {
        plugin.scope
    }
    override val pluginManager = plugin.server.pluginManager
    override val pluginInstance = plugin

    private val moduleLoader = CombatModuleLoaderImpl

    override suspend fun onLoad() {
        moduleLoader.onLoad()
    }

    override suspend fun onEnable() {
        moduleLoader.onEnable()

        OnlineListener.register()
    }

    override suspend fun onDisable() {
        moduleLoader.onDisable()
    }

    override fun launch(
        context: CoroutineContext,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ) = plugin.launch(context, start, block)

    override fun entityDispatcher(entity: Entity) = plugin.entityDispatcher(entity)
    override fun regionDispatcher(location: Location) = plugin.regionDispatcher(location)
}