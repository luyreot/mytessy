package com.teoryul.mytesy.ui.appliancedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.ui.helper.ApplianceProgramMode
import com.teoryul.mytesy.ui.helper.getProgram
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplianceDetailViewModel(
    applianceUseCase: ApplianceUseCase,
    applianceId: String
) : ViewModel() {

    // TODO maybe add method in db to fetch appliance by id
    private val applianceFlow: Flow<ApplianceEntity?> =
        applianceUseCase.observeAppliances()
            .map { list -> list.firstOrNull { it.id == applianceId } }
            .distinctUntilChanged()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(ApplianceDetailUiState())
    val uiState: StateFlow<ApplianceDetailUiState> = combine(
        applianceFlow,
        _uiState
    ) { appliance, state ->
        val activeProgram = getProgram(appliance)
        val newProgram = if (state.selectedProgram == ApplianceProgramMode.Undefined)
            activeProgram
        else
            state.selectedProgram

        // TODO get current eco mode
        // TODO get boost on/off state
        state.copy(
            activeProgram = activeProgram,
            selectedProgram = newProgram,
            appliance = appliance
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ApplianceDetailUiState()
    )

    fun onProgramSelected(program: ApplianceProgramMode) {
        _uiState.update { it.copy(selectedProgram = program) }
        // TODO make api call to update the program
    }

    fun onEcoModesClick() {
        _uiState.update {
            if (it.isEcoModeUpdating) it
            else it.copy(ecoModesExpanded = !it.ecoModesExpanded)
        }
    }

    fun onDismissEcoModesMenu() {
        _uiState.update { it.copy(ecoModesExpanded = false) }
    }

    fun onInfoMenuClick() {
        _uiState.update { it.copy(infoMenuExpanded = !it.infoMenuExpanded) }
    }

    fun onDismissInfoMenu() {
        _uiState.update { it.copy(infoMenuExpanded = false) }
    }

    fun onEcoModeSelected(mode: EcoMode) {
        if (_uiState.value.selectedEcoMode == mode) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ecoModesExpanded = false,
                    isEcoModeUpdating = true
                )
            }

            try {
                // TODO Mock API call - implement real api call
                delay(900)
                val shouldFail = false
                if (shouldFail) error("Mock error")

                _uiState.update {
                    it.copy(
                        selectedEcoMode = mode,
                        isEcoModeUpdating = false
                    )
                }
            } catch (_: Throwable) {
                _uiState.update { it.copy(isEcoModeUpdating = false) }
                emitToastMessage("Failed to change mode")
            }
        }
    }

    fun emitToastMessage(message: String) {
        viewModelScope.launch {
            _events.emit(UiEvent.Toast(message))
        }
    }
}