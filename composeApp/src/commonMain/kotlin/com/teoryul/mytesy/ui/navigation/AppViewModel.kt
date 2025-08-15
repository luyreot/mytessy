package com.teoryul.mytesy.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.util.AppLogger
import com.teoryul.mytesy.util.AppVisibilityProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppViewModel(
    private val visibilityProvider: AppVisibilityProvider,
    private val applianceUseCase: ApplianceUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            visibilityProvider.visibility.collectLatest {
                AppLogger.d("$it")
            }
        }
    }
}