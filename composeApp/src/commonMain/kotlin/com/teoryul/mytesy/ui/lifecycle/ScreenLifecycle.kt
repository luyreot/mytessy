package com.teoryul.mytesy.ui.lifecycle

interface ScreenLifecycle {
    fun onCreate() {}
    fun onStart() {}
    fun onResume() {}
    fun onPause() {}
    fun onStop() {}
    fun onDestroy() {}
}