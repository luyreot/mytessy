package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandlerPlatform(
    enabled: Boolean,
    onBack: () -> Unit
)