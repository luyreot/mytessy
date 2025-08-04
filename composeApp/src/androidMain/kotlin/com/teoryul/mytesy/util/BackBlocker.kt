package com.teoryul.mytesy.util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun RegisterBackBlocker(enabled: Boolean) {
    if (enabled) {
        BackHandler(enabled = true) {
            // Do nothing — block system back
        }
    }
}