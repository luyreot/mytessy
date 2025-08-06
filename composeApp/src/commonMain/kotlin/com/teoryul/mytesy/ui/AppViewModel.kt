package com.teoryul.mytesy.ui

import androidx.lifecycle.ViewModel
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase

class AppViewModel(
    private val applianceUseCase: ApplianceUseCase
) : ViewModel() {

}