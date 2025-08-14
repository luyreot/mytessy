package com.teoryul.mytesy.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.ui.common.ComingSoon
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    stateHolderKey: String,
    viewModel: SettingsViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ComingSoon()
    }
}