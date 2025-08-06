package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen {
    object Welcome : Screen()
    object Login : Screen()
    object ComingSoon : Screen()
    object Home : Screen()
    object AddAppliance : Screen()
    object Notifications : Screen()
    object Settings : Screen()
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
                "Home" -> Screen.Home
                "AddAppliance" -> Screen.AddAppliance
                "Notifications" -> Screen.Notifications
                "Settings" -> Screen.Settings
                else -> Screen.Welcome
            }
        }
    }
)