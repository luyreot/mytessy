package com.teoryul.mytesy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.infra.polling.PollKey
import com.teoryul.mytesy.infra.polling.PollingManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val applianceUseCase: ApplianceUseCase,
    private val polling: PollingManager
) : ViewModel() {

    val appliances: StateFlow<List<ApplianceEntity>> =
        applianceUseCase.observeAppliances()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onPullToRefresh() {
        polling.refreshNow(PollKey.APPLIANCES)
    }
}