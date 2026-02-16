package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.ui.common.showToast
import com.teoryul.mytesy.ui.helper.ApplianceProgramMode
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

    val isViewingManualProgram = uiState.selectedProgram == ApplianceProgramMode.Manual
    val isViewingActiveProgram = uiState.selectedProgram == uiState.activeProgram
    val activateLabel = if (isViewingActiveProgram) "Deactivate" else "Activate"

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProgramChipsRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                active = uiState.activeProgram,
                selected = uiState.selectedProgram,
                onSelect = viewModel::onProgramSelected
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TemperatureCenter(
                    modifier = Modifier.fillMaxWidth(),
                    currentTemp = currentTemp,
                    targetTemp = targetTemp,
                    isPowerOn = isPowerOn,
                    statusText = statusText,
                    isHeating = isHeating
                )

                Spacer(Modifier.height(12.dp))

                ProgramActionButtons(
                    isViewingManualProgram = isViewingManualProgram,
                    activateLabel = activateLabel,
                    onToggleProgramClick = {
                        viewModel.onToggleProgramClick()
                    },
                    onEditProgramClick = {
                        // TODO implement edit program screen and navigate to it
                        viewModel.emitToastMessage("Coming Soon")
                    }
                )
            }

            // TODO hook up all boolean flags
            Row(
                modifier = Modifier
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
    }
}

@Composable
private fun ProgramActionButtons(
    isViewingManualProgram: Boolean,
    activateLabel: String,
    onToggleProgramClick: () -> Unit,
    onEditProgramClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isViewingManualProgram)
            Arrangement.Center
        else
            Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = onToggleProgramClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E006C),
                contentColor = Color.White
            )
        ) {
            Text(activateLabel)
        }

        if (isViewingManualProgram) return@Row

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onEditProgramClick,
            border = BorderStroke(1.dp, Color(0xFF5E006C)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF5E006C)
            )
        ) {
            Text("Edit")
        }
    }
}