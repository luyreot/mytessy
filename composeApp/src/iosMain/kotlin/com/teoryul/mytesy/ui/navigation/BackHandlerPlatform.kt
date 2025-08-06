package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandlerPlatform(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // No-op on iOS. Back handled via UI (e.g., arrow buttons)
}