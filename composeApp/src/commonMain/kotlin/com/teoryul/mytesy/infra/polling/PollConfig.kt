package com.teoryul.mytesy.infra.polling

data class PollConfig(
    val intervalMs: Long,
    val immediateOnStart: Boolean = true,
    val maxBackoffMs: Long = 5 * 60_000L
)