package com.teoryul.mytesy.ui.login

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginTriggered: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
    val emailError: String? = null
)