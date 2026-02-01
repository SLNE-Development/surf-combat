package dev.slne.surf.combat.api.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

abstract class CombatJob(
    val scope: CoroutineScope,
    val sleepDuration: Duration
) {
    private var job: Job? = null

    fun start() {
        if (job?.isActive == true) {
            error("Job is already started.")
        }

        job = scope.launch {
            while (true) {
                tick()
                delay(sleepDuration)
            }
        }
    }

    abstract fun tick()

    fun stop() {
        job?.cancel()
        job = null
    }
}