package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

/**
 * iOS actual:
 * - Compose runs inside a ComposeUIViewController which typically isn't recreated on rotate,
 *   so a simple per-screen ViewModelStore is enough.
 * - We clear the store when the Composable truly leaves composition (i.e., when you pop).
 */
@Composable
actual fun WithScreenViewModelStore(
    key: String,
    content: @Composable () -> Unit
) {
    val store = remember(key) { ViewModelStore() }
    val owner = remember(store) {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = store
        }
    }

    DisposableEffect(key) {
        onDispose { store.clear() }
    }

    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}