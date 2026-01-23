package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplianceDetailScreen(
    stateHolderKey: String,
    viewModel: ApplianceDetailViewModel = koinViewModel(),
    appliance: ApplianceEntity,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}