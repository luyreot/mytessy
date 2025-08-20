package com.teoryul.mytesy.infra

import kotlinx.coroutines.flow.StateFlow

enum class AppVisibility {
    FOREGROUND,
    BACKGROUND
}

expect class AppVisibilityProvider {
    val visibility: StateFlow<AppVisibility>
}