package com.teoryul.mytesy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.infra.polling.PollKey
import com.teoryul.mytesy.infra.polling.PollingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val applianceUseCase: ApplianceUseCase,
    private val polling: PollingManager
) : ViewModel() {

    val appliances: StateFlow<List<ApplianceEntity>> =
        applianceUseCase.observeAppliances()
            .onEach { list ->
                val ids = list.map { it.id }.toSet()
                _applianceUiFlags.update { map ->
                    map
                        .filterKeys(ids::contains)
                        .mapValues { (_, flags) ->
                            if (flags.errorMessage == null) flags
                            else flags.copy(errorMessage = null)
                        }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Pull to refresh appliance list
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    // Individual appliance UI flags (state not persisted in DB)
    private val _applianceUiFlags = MutableStateFlow<Map<String, ApplianceUiFlags>>(emptyMap())
    val applianceUiFlags: StateFlow<Map<String, ApplianceUiFlags>> = _applianceUiFlags

    fun onPullToRefresh() {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        polling.refreshNow(PollKey.APPLIANCES) {
            _isRefreshing.value = false
        }
    }

    fun onAppliancePowerToggle(appliance: ApplianceEntity, enabled: Boolean) {
        viewModelScope.launch {
            updateApplianceFlags(appliance.id.orEmpty()) { it.copy(showPowerButtonSpinner = true) }
            val result = applianceUseCase.toggleAppliancePower(appliance, enabled)
            updateApplianceFlags(appliance.id.orEmpty()) {
                it.copy(
                    showPowerButtonSpinner = false,
                    errorMessage = (result as? ApplianceUseCase.Result.Fail)?.error?.message
                )
            }
        }
    }

    private fun updateApplianceFlags(id: String, block: (ApplianceUiFlags) -> ApplianceUiFlags) {
        _applianceUiFlags.update { map ->
            val current = map[id] ?: ApplianceUiFlags()
            map + (id to block(current))
        }
    }
}