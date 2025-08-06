package com.teoryul.mytesy.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerPlatform(
    enabled: Boolean,
    onBack: () -> Unit
) {
    BackHandler(enabled = enabled, onBack = onBack)
}