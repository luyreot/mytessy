package com.teoryul.mytesy.ui.welcome

sealed interface WelcomeViewEffect {
    data object NavigateToHome : WelcomeViewEffect
}