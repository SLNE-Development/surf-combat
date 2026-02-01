package dev.slne.surf.combat.api

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.core.api.util.requiredService
import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.PluginManager

private val instance = requiredService<CombatInstance>()

interface CombatInstance {
    val pluginScope: CoroutineScope
    val pluginManager: PluginManager
    val pluginInstance: SuspendingJavaPlugin

    suspend fun onLoad()
    suspend fun onEnable()
    suspend fun onDisable()

    companion object : CombatInstance by instance {
        val INSTANCE get() = instance
    }
}