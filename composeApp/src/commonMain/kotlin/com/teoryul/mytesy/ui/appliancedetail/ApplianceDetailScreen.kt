package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.ui.common.showToast
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ApplianceDetailScreen(
    stateHolderKey: String,
    applianceId: String,
    navigateToComingSoon: () -> Unit
) {
    val viewModel: ApplianceDetailViewModel = koinViewModel(
        key = stateHolderKey, // TODO do I need this?
        parameters = { parametersOf(applianceId) }
    )

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.Toast -> showToast(event.message)
            }
        }
    }

    DisposableEffect(stateHolderKey) {
        onDispose {
            viewModel.onDismissEcoModesMenu()
            viewModel.onDismissInfoMenu()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    val currentTemp = uiState.appliance?.statusTmpC
    val targetTemp = uiState.appliance?.statusTmpR
    val isPowerOn = (uiState.appliance?.statusPwr == "1")
    val statusText = (uiState.appliance?.statusText ?: "").ifBlank { "--" }
    val isHeating = statusText.equals("heating", true)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProgramChipsRow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 12.dp),
            active = uiState.activeProgram,
            selected = uiState.selectedProgram,
            onSelect = viewModel::onProgramSelected
        )

        TemperatureCenter(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            currentTemp = currentTemp,
            targetTemp = targetTemp,
            isPowerOn = isPowerOn,
            statusText = statusText,
            isHeating = isHeating
        )

        EcoModesPopup(
            expanded = uiState.ecoModesExpanded,
            isSelectedEcoMode = uiState.selectedEcoMode != EcoMode.Off,
            onSelect = viewModel::onEcoModeSelected,
            onDismiss = viewModel::onDismissEcoModesMenu
        )

        InfoMenuPopup(
            expanded = uiState.infoMenuExpanded,
            onDismiss = viewModel::onDismissInfoMenu,
            onItemClick = { item ->
                // TODO implement each screen
                navigateToComingSoon()
            }
        )

        // TODO hook up boolean all flags
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EcoModesQuickAction(
                selectedEcoMode = uiState.selectedEcoMode,
                isLoading = uiState.isEcoModeUpdating,
                onClick = viewModel::onEcoModesClick
            )

//            ApplianceQuickAction(
//                icon = Icons.Default.Eco,
//                label = "Modes",
//                isEnabled = true,
//                isActive = false,
//                onClick = { viewModel.emitToastMessage("Coming Soon") }
//            )

            ApplianceQuickAction(
                icon = Icons.Default.RocketLaunch,
                label = "Boost",
                isEnabled = true,
                isActive = false,
                onClick = { viewModel.emitToastMessage("Coming Soon") }
            )

            ApplianceQuickAction(
                icon = Icons.Default.PowerSettingsNew,
                label = "On/Off",
                isEnabled = true,
                isActive = true,
                onClick = { /* TODO add api call */ }
            )

            ApplianceQuickAction(
                icon = Icons.Default.Info,
                label = "Info",
                isEnabled = true,
                isActive = false,
                onClick = { viewModel.onInfoMenuClick() }
            )
        }
    }
}