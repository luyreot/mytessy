package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen {
    object Welcome : Screen()
    object Login : Screen()
    object ComingSoon : Screen()
}

val ScreenBackStackSaver: Saver<List<Screen>, List<String>> = Saver(
    save = { screenList ->
        screenList.mapNotNull { it::class.simpleName }
    },
    restore = { savedList ->
        savedList.map {
            when (it) {
                "Welcome" -> Screen.Welcome
                "Login" -> Screen.Login
                "ComingSoon" -> Screen.ComingSoon
                else -> Screen.Welcome
            }
        }
    }
)