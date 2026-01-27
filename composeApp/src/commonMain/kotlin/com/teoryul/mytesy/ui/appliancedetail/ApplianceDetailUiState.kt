package com.teoryul.mytesy.ui.appliancedetail

import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.ui.helper.ApplianceProgramMode

data class ApplianceDetailUiState(
    val activeProgram: ApplianceProgramMode = ApplianceProgramMode.Undefined,
    val selectedProgram: ApplianceProgramMode = ApplianceProgramMode.Undefined,
    val appliance: ApplianceEntity? = null,
    val ecoModesExpanded: Boolean = false,
    val selectedEcoMode: EcoMode = EcoMode.Off,
    val isEcoModeUpdating: Boolean = false
)