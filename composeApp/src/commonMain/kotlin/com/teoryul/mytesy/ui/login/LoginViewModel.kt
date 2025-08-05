package com.teoryul.mytesy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.domain.usecase.LoginUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private var emailValidationJob: Job? = null

    fun onEmailChanged(newEmail: String) {
        emailValidationJob?.cancel()

        val error = when {
            newEmail.isBlank() -> "Email cannot be empty"
            !newEmail.isValidEmail() -> "Invalid email format"
            else -> null
        }

        _viewState.update {
            val newState = if (error == null) {
                it.copy(email = newEmail, emailError = null)
            } else {
                it.copy(email = newEmail)
            }
            newState.copy(isFormValid = validateForm(newState))
        }

        if (error == null) return

        emailValidationJob = viewModelScope.launch {
            delay(500L)
            _viewState.update { it.copy(emailError = error) }
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _viewState.update {
            val newState = it.copy(password = newPassword)
            newState.copy(isFormValid = validateForm(newState))
        }
    }

    private fun validateForm(state: LoginViewState): Boolean {
        return state.email.isValidEmail() && state.password.isNotBlank()
    }

    private fun String.isValidEmail(): Boolean {
        return isNotBlank() && EMAIL_REGEX.matches(this)
    }

    fun onLoginClicked() {
        _viewState.update { it.copy(loginTriggered = true) }
    }

    fun performLoginIfTriggered() {
        val state = viewState.value
        if (!state.loginTriggered) return

        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }

            when (val result = loginUseCase(email = state.email, password = state.password)) {
                is LoginUseCase.LoginResult.Fail -> {
                    _viewState.update { it.copy(errorMessage = result.message) }
                }

                is LoginUseCase.LoginResult.LoginSuccess -> {
                    _viewState.update { it.copy(errorMessage = null) }
                    // TODO: Emit ViewEffect to go to home screen
                }
            }

            _viewState.update { it.copy(isLoading = false, loginTriggered = false) }
        }
    }

    private companion object {
        private val EMAIL_REGEX = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
    }
}