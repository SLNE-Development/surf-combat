package dev.slne.surf.combat.api.module

import dev.slne.surf.combat.api.CombatInstance
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.bukkit.api.event.unregister
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.coroutines.*
import org.bukkit.event.Listener
import kotlin.coroutines.CoroutineContext

abstract class CombatModule(
    val name: String,
    val events: ObjectSet<Listener> = objectSetOf()
) {
    private val log = logger()
    val moduleScope = CoroutineScope(
        CombatInstance.pluginScope.coroutineContext + CoroutineName("CombatModule-$name") + CoroutineExceptionHandler { context, throwable ->
            log.atSevere()
                .withCause(throwable)
                .log("Exception in coroutine ${context[CoroutineName]} of module $name")
        }
    )

    suspend fun internalOnLoad() {
        events.forEach(Listener::register)
        onLoad()
    }

    suspend fun internalOnEnable() {
        onEnable()
    }

    suspend fun internalOnDisable() {
        moduleScope.cancel("Module $name is being disabled")
        events.forEach(Listener::unregister)
        onDisable()
    }

    open suspend fun onLoad() {}
    open suspend fun onEnable() {}
    open suspend fun onDisable() {}

    fun launch(
        context: CoroutineContext = moduleScope.coroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        if (!moduleScope.isActive) {
            return Job()
        }

        return moduleScope.launch(context, start, block)
    }
}