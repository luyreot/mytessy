package com.teoryul.mytesy.ui.addappliance

import androidx.compose.runtime.Composable
import com.teoryul.mytesy.ui.comingsoon.ComingSoonScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddApplianceScreen(
    stateHolderKey: String,
    viewModel: AddApplianceViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    // TODO Implement
    //Box(modifier = Modifier.fillMaxSize()) {}
    ComingSoonScreen(onBackClick = onBackClick)
}