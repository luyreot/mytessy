package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable

@Composable
expect fun WithRetainedTabViewModelStore(
    key: String,
    content: @Composable () -> Unit
)

@Composable
expect fun ClearAllRetainedTabs()

@Composable
expect fun ClearRetainedTabs(vararg keys: String)