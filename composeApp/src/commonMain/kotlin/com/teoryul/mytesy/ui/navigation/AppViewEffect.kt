package com.teoryul.mytesy.ui.navigation

sealed interface AppViewEffect {
    data object SessionLost : AppViewEffect
}