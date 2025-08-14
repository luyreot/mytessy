package com.teoryul.mytesy.ui.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.ui.common.ComingSoon
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    stateHolderKey: String,
    viewModel: NotificationsViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ComingSoon()
    }
}