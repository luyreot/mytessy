package com.teoryul.mytesy.infra

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class AppVisibilityProvider {

    private val _visibility = MutableStateFlow(AppVisibility.BACKGROUND)

    actual val visibility: StateFlow<AppVisibility> = _visibility

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> _visibility.value = AppVisibility.FOREGROUND
                    Lifecycle.Event.ON_STOP -> _visibility.value = AppVisibility.BACKGROUND
                    else -> Unit
                }
            }
        )
    }
}