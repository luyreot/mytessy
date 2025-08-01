package com.teoryul.mytesy.ui.login

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginTriggered: Boolean = false
)