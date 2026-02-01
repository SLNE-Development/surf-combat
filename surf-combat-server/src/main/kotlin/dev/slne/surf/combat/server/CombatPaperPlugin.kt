package dev.slne.surf.combat.server

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.combat.api.CombatInstance
import org.bukkit.plugin.java.JavaPlugin

class CombatPaperPlugin : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {
        CombatInstance.onLoad()
    }

    override suspend fun onEnableAsync() {
        CombatInstance.onEnable()
    }

    override suspend fun onDisableAsync() {
        CombatInstance.onDisable()
    }
}

val plugin: CombatPaperPlugin get() = JavaPlugin.getPlugin(CombatPaperPlugin::class.java)