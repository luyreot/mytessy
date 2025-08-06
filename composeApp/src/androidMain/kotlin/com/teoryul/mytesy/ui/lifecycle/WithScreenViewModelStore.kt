package com.teoryul.mytesy.ui.lifecycle

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

/**
 * Activity-scoped VM that holds a map of ViewModelStores (one per "screen key").
 * This survives configuration changes, so per-screen ViewModels survive rotation.
 */
class ScreenStoresVM : ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    fun ownerFor(key: String): ViewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore =
            stores.getOrPut(key) { ViewModelStore() }
    }

    fun clear(key: String) {
        stores.remove(key)?.clear()
    }

    override fun onCleared() {
        // Cleanup if the Activity is truly finishing
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}

/**
 * Android actual:
 * - Uses an Activity-scoped ViewModel (ScreenStoresVM) to persist ViewModelStores across rotation.
 * - Clears a store when the screen is *actually popped*, not when rotating.
 */
@Composable
actual fun WithScreenViewModelStore(
    key: String,
    content: @Composable () -> Unit
) {
    // Owner of the Activity's ViewModelStore (survives configuration changes)
    val activityOwner = LocalViewModelStoreOwner.current
        ?: error("No Activity ViewModelStoreOwner found")

    // Fetch the ScreenStoresVM from the Activity scope
    val storesVM = remember(activityOwner) {
        ViewModelProvider(activityOwner)[ScreenStoresVM::class.java]
    }

    // Get/create a per-key owner
    val owner = remember(key) { storesVM.ownerFor(key) }

    // Only clear this screen's store if we are not changing configuration (i.e., not rotating)
    val activity = LocalActivity.current
    DisposableEffect(key) {
        onDispose {
            val rotating = activity?.isChangingConfigurations == true
            if (!rotating) {
                storesVM.clear(key)
            }
        }
    }

    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}