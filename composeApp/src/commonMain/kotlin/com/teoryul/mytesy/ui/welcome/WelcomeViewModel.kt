package com.teoryul.mytesy.ui.welcome

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeViewModel(

) : ViewModel() {

    private val _viewState = MutableStateFlow(WelcomeViewState())
    val viewState: StateFlow<WelcomeViewState> = _viewState.asStateFlow()

}