package com.teoryul.mytesy.util

import kotlinx.coroutines.flow.StateFlow

enum class AppVisibility {
    Foreground,
    Background
}

expect class AppVisibilityProvider {
    val visibility: StateFlow<AppVisibility>
}