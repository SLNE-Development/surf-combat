package dev.slne.surf.combat.api.event

import org.bukkit.event.Cancellable

open class CancellableCombatEvent : CombatEvent(), Cancellable {
    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }
}