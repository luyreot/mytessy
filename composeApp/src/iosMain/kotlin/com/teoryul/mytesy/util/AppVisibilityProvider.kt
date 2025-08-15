package com.teoryul.mytesy.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification

actual class AppVisibilityProvider {

    private val _visibility = MutableStateFlow(AppVisibility.Background)

    actual val visibility: StateFlow<AppVisibility> = _visibility

    init {
        val center = NSNotificationCenter.Companion.defaultCenter

        center.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.Companion.mainQueue
        ) { _visibility.value = AppVisibility.Foreground }

        center.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = NSOperationQueue.Companion.mainQueue
        ) { _visibility.value = AppVisibility.Background }
    }
}