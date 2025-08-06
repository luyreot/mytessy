package com.teoryul.mytesy.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Because the observer is attached after the owner may already be CREATED/STARTED/RESUMED,
 * ON_CREATE can still be seen to not fire at all (or appear “late”) on Android.
 * That's normal because the event already happened before the observer was added.
 */
@Composable
fun BindLifecycle(target: ScreenLifecycle) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Ensure onCreate runs even if we attach after CREATED
    //LaunchedEffect(target) { target.onCreate() }

    DisposableEffect(lifecycleOwner, target) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> target.onCreate()
                Lifecycle.Event.ON_START -> target.onStart()
                Lifecycle.Event.ON_RESUME -> target.onResume()
                Lifecycle.Event.ON_PAUSE -> target.onPause()
                Lifecycle.Event.ON_STOP -> target.onStop()
                Lifecycle.Event.ON_DESTROY -> target.onDestroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}