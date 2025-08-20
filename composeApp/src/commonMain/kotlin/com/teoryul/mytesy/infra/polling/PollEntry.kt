package com.teoryul.mytesy.infra.polling

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

data class PollEntry(
    var config: PollConfig,
    val runner: suspend () -> PollDecision,
    val mutex: Mutex = Mutex(),
    var job: Job? = null,
    var nextDelayMs: Long = config.intervalMs
)