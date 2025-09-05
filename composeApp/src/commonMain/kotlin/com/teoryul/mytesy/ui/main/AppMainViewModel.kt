package com.teoryul.mytesy.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.domain.session.SessionStatus
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.domain.usecase.RestoreSessionUseCase
import com.teoryul.mytesy.infra.AppVisibility
import com.teoryul.mytesy.infra.AppVisibilityProvider
import com.teoryul.mytesy.infra.polling.PollConfig
import com.teoryul.mytesy.infra.polling.PollKey
import com.teoryul.mytesy.infra.polling.PollDecision
import com.teoryul.mytesy.infra.polling.PollingManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AppMainViewModel(
    private val visibilityProvider: AppVisibilityProvider,
    private val sessionManager: SessionManager,
    private val restoreSessionUseCase: RestoreSessionUseCase,
    private val applianceUseCase: ApplianceUseCase,
    private val polling: PollingManager
) : ViewModel() {

    private val _viewEffect = MutableSharedFlow<AppMainViewEffect>()
    val viewEffect: SharedFlow<AppMainViewEffect> = _viewEffect

    init {
        // Gate all polling by session + foreground
        viewModelScope.launch {
            combine(sessionManager.status, visibilityProvider.visibility) { s, v ->
                (s == SessionStatus.LOGGED_IN) && (v == AppVisibility.FOREGROUND)
            }.collectLatest { canRun ->
                polling.setActive(canRun)
            }
        }

        // Appliances job
        polling.addOrReplace(
            key = PollKey.APPLIANCES,
            config = PollConfig(
                intervalMs = 15_000L,
                immediateOnStart = true,
                maxBackoffMs = 5 * 60_000L
            )
        ) {
            when (val result = applianceUseCase.syncAppliances()) {
                ApplianceUseCase.Result.Success -> PollDecision.STANDARD

                is ApplianceUseCase.Result.Fail -> {
                    if (result.error !is ErrorResult.Unauthenticated) {
                        return@addOrReplace PollDecision.BACKOFF
                    }

                    when (restoreSessionUseCase()) {
                        RestoreSessionUseCase.Result.SessionRestored -> PollDecision.IMMEDIATE
                        RestoreSessionUseCase.Result.SessionNotFound -> {
                            sessionManager.clear()
                            _viewEffect.emit(AppMainViewEffect.SessionLost)
                            PollDecision.BACKOFF
                        }
                    }
                }
            }
        }
    }
}