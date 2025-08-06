package com.teoryul.mytesy.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.usecase.RestoreSessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val restoreSessionUseCase: RestoreSessionUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(WelcomeViewState())
    val viewState: StateFlow<WelcomeViewState> = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<WelcomeViewEffect>()
    val viewEffect: SharedFlow<WelcomeViewEffect> = _viewEffect

    fun restoreSession() {
        viewModelScope.launch {
            when (restoreSessionUseCase()) {
                RestoreSessionUseCase.SessionResult.SessionNotFound -> _viewState.update {
                    it.copy(isLoading = false)
                }

                RestoreSessionUseCase.SessionResult.SessionRestored -> _viewEffect.emit(
                    WelcomeViewEffect.NavigateToHome
                )
            }
        }
    }
}