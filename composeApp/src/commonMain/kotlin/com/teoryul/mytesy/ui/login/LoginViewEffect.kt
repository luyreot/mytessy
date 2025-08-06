package com.teoryul.mytesy.ui.login

sealed interface LoginViewEffect {
    data object NavigateToHome : LoginViewEffect
}