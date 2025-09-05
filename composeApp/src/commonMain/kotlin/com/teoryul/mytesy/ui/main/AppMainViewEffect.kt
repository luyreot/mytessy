package com.teoryul.mytesy.ui.main

sealed interface AppMainViewEffect {
    data object SessionLost : AppMainViewEffect
}