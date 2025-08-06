package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable

@Composable
expect fun WithScreenViewModelStore(
    key: String,
    content: @Composable () -> Unit
)