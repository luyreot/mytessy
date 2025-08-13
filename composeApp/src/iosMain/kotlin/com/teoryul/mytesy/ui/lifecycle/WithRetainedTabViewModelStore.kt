package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

private object TabStores {

    val stores = mutableMapOf<String, ViewModelStore>()

    fun ownerFor(tabKey: String): ViewModelStoreOwner =
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore =
                stores.getOrPut(tabKey) { ViewModelStore() }
        }

    fun clear(key: String) {
        stores.remove(key)?.clear()
    }

    fun clearAll() {
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}

@Composable
actual fun ClearAllRetainedTabs() {
    TabStores.clearAll()
}

@Composable
actual fun ClearRetainedTabs(vararg keys: String) {
    keys.forEach { TabStores.clear(it) }
}

@Composable
actual fun WithRetainedTabViewModelStore(
    key: String,
    content: @Composable () -> Unit
) {
    val owner = remember(key) { TabStores.ownerFor(key) }

    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}