package com.teoryul.mytesy.infra.polling

import com.teoryul.mytesy.infra.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

/**
 * Lightweight registry + runner for periodic jobs.
 * - Add/replace jobs with a key, a config, and a 'runner' that returns true on success, false on failure.
 * - setActive(true/false) globally starts/stops all registered jobs (use this for session/visibility gating).
 * - refreshNow(key): pauses that job, runs once immediately, then resumes periodic at the base interval.
 */
class PollingManager(
    private val scope: CoroutineScope
) {

    private val entries = mutableMapOf<PollKey, PollEntry>()

    @Volatile
    private var active = false

    fun addOrReplace(
        key: PollKey,
        config: PollConfig,
        runner: suspend () -> PollDecision
    ) {
        // Stop old if exists
        entries.remove(key)?.let { it.job?.cancel() }
        entries[key] = PollEntry(config = config, runner = runner)
        if (active) start(key, immediate = config.immediateOnStart)
    }

    fun remove(key: PollKey) {
        entries.remove(key)?.job?.cancel()
    }

    fun setActive(enable: Boolean) {
        if (enable == active) return
        active = enable
        if (!active) {
            entries.values.forEach { it.job?.cancel(); it.job = null }
        } else {
            entries.forEach { (key, e) -> start(key, immediate = e.config.immediateOnStart) }
        }
    }

    /**
     * Pull-to-refresh for a single job: pause → one run (swallow errors, backoff handled on next loop) → resume at base interval (no extra immediate).
     */
    fun refreshNow(key: PollKey) {
        val entry = entries[key] ?: return
        entry.job?.cancel()
        entry.job = null

        scope.launch {
            entry.mutex.withLock {
                runCatching { entry.runner() }.onFailure { AppLogger.e(it) }
            }
            if (active) start(key, immediate = false)
        }
    }

    fun updateConfig(key: PollKey, newConfig: PollConfig) {
        val entry = entries[key] ?: return
        entry.config = newConfig
        entry.job?.cancel()
        entry.job = null
        if (active) start(key, immediate = newConfig.immediateOnStart)
    }

    private fun start(key: PollKey, immediate: Boolean) {
        val e = entries[key] ?: return
        e.job?.cancel()
        e.nextDelayMs = e.config.intervalMs

        e.job = scope.launch {
            if (immediate) {
                val pollNext = runOnce(e)
                e.nextDelayMs = when (pollNext) {
                    PollDecision.STANDARD -> e.config.intervalMs
                    PollDecision.BACKOFF -> backoff(e.nextDelayMs, e.config.maxBackoffMs)
                    PollDecision.IMMEDIATE -> 0
                }
            }
            while (isActive) {
                delay(e.nextDelayMs)
                val pollNext = runOnce(e)
                e.nextDelayMs = when (pollNext) {
                    PollDecision.STANDARD -> e.config.intervalMs
                    PollDecision.BACKOFF -> backoff(e.nextDelayMs, e.config.maxBackoffMs)
                    PollDecision.IMMEDIATE -> 0
                }
            }
        }
    }

    private suspend fun runOnce(e: PollEntry): PollDecision =
        runCatching {
            e.mutex.withLock { e.runner() }
        }.getOrDefault(PollDecision.BACKOFF)

    private fun backoff(curr: Long, cap: Long) = (curr * 2).coerceAtMost(cap)
}