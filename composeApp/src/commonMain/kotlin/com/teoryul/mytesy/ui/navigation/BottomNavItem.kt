package com.teoryul.mytesy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
) {
    object Home :
        BottomNavItem("Home", Icons.Default.Home, Screen.Home)

    object AddAppliance :
        BottomNavItem("Add appliance", Icons.Default.AddCircleOutline, Screen.AddAppliance)

    object Notifications :
        BottomNavItem("Notifications", Icons.Default.NotificationsNone, Screen.Notifications)

    object Settings :
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings)

    companion object {
        val items = listOf(Home, AddAppliance, Notifications, Settings)
    }
}