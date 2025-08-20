package com.teoryul.mytesy.infra

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AppScopes(
    onError: (Throwable) -> Unit = { AppLogger.e(it) }
) {

    private val handler = CoroutineExceptionHandler { _, e -> onError(e) }

    val appScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default + handler + CoroutineName("AppScope")
    )
}