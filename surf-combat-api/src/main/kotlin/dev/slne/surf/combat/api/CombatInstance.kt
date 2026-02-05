package dev.slne.surf.combat.api

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.asyncDispatcher
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.mainDispatcher
import dev.slne.surf.surfapi.core.api.util.requiredService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.PluginManager
import kotlin.coroutines.CoroutineContext

private val instance = requiredService<CombatInstance>()

interface CombatInstance {
    val pluginScope: CoroutineScope
    val pluginManager: PluginManager
    val pluginInstance: SuspendingJavaPlugin

    fun launch(
        context: CoroutineContext = mainDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    val globalRegionDispatcher: CoroutineContext get() = pluginInstance.globalRegionDispatcher
    val asyncDispatcher: CoroutineContext get() = pluginInstance.asyncDispatcher
    val mainDispatcher: CoroutineContext get() = pluginInstance.mainDispatcher

    fun regionDispatcher(location: Location): CoroutineContext
    fun entityDispatcher(entity: Entity): CoroutineContext

    suspend fun onLoad()
    suspend fun onEnable()
    suspend fun onDisable()

    companion object : CombatInstance by instance {
        val INSTANCE get() = instance
    }
}