package com.teoryul.mytesy.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BroadcastOnHome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    stateHolderKey: String,
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToComingSoon: () -> Unit // TODO temporary navigation until the settings screens are done
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            SettingClickableItem(
                icon = Icons.Filled.Person,
                label = "Account Details",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.Password,
                label = "Change password",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.Cloud,
                label = "tesyCloud",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.BroadcastOnHome,
                label = "Google Home",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.Book,
                label = "Tutorials",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.Info,
                label = "Application Information",
                onClick = navigateToComingSoon
            )
            SettingClickableItem(
                icon = Icons.Filled.Logout,
                label = "Log out",
                onClick = navigateToComingSoon
            )
            SettingTextItem(
                label = "App version: 1 (1)",
                showDivider = false
            )
        }
    }
}