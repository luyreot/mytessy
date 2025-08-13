package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

/**
 * Activity-scoped VM that holds ViewModelStores per bottom tab.
 * These DO NOT clear on tab switch.
 * They survive rotation and are cleared when the Activity is actually destroyed.
 */
class TabStoresVM : ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    fun ownerFor(tabKey: String): ViewModelStoreOwner =
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore =
                stores.getOrPut(tabKey) { ViewModelStore() }
        }

    fun clear(tabKey: String) {
        stores.remove(tabKey)?.clear()
    }

    fun clearAll() {
        stores.values.forEach { it.clear() }
        stores.clear()
    }

    override fun onCleared() {
        clearAll()
    }
}

@Composable
actual fun ClearAllRetainedTabs() {
    val activityOwner = LocalViewModelStoreOwner.current
        ?: error("No Activity ViewModelStoreOwner found")

    val tabsVM = remember(activityOwner) {
        ViewModelProvider(activityOwner)[TabStoresVM::class.java]
    }
    tabsVM.clearAll()
}

@Composable
actual fun ClearRetainedTabs(vararg keys: String) {
    val activityOwner = LocalViewModelStoreOwner.current
        ?: error("No Activity ViewModelStoreOwner found")

    val tabsVM = remember(activityOwner) {
        ViewModelProvider(activityOwner)[TabStoresVM::class.java]
    }
    keys.forEach { tabsVM.clear(it) }
}

@Composable
actual fun WithRetainedTabViewModelStore(
    key: String,
    content: @Composable () -> Unit
) {
    val activityOwner = LocalViewModelStoreOwner.current
        ?: error("No Activity ViewModelStoreOwner found")

    val tabsVM = remember(activityOwner) {
        ViewModelProvider(activityOwner)[TabStoresVM::class.java]
    }
    val owner = remember(key) { tabsVM.ownerFor(key) }

    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}