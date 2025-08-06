package com.teoryul.mytesy.ui.settings

import androidx.compose.runtime.Composable
import com.teoryul.mytesy.ui.comingsoon.ComingSoonScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    // TODO Implement
    //Box(modifier = Modifier.fillMaxSize()) {}
    ComingSoonScreen(onBackClick = onBackClick)
}