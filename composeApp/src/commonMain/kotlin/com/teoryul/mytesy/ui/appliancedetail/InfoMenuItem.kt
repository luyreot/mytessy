package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class InfoMenuItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false
)

val defaultInfoItems = listOf(
    InfoMenuItem("rename", "Rename appliance", Icons.Filled.Edit),
    InfoMenuItem("device_info", "Device info", Icons.Filled.Description),
    InfoMenuItem("stats", "Statistics", Icons.Filled.QueryStats),
    InfoMenuItem("calculator", "Calculator", Icons.Filled.Calculate),
    InfoMenuItem("settings", "Settings", Icons.Filled.Settings),
    InfoMenuItem("delete", "Delete appliance", Icons.Filled.Delete, isDestructive = true)
)