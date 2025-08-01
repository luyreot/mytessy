package com.teoryul.mytesy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teoryul.mytesy.data.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val loginRepository = LoginRepository()

    fun onEmailChanged(newEmail: String) {
        _viewState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChanged(newPassword: String) {
        _viewState.update { it.copy(password = newPassword) }
    }

    fun onLoginClicked() {
        _viewState.update { it.copy(loginTriggered = true) }
    }

    fun performLoginIfTriggered() {
        val state = viewState.value
        if (!state.loginTriggered) return


        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }

            try {
                val loginResponse = loginRepository.login(state.email, state.password)
                println("✅ Login success. Token: ${loginResponse.token}")

                val oldLoginResponse = loginRepository.loginOldApp(
                    email = loginResponse.email,
                    password = loginResponse.password,
                    userID = loginResponse.userID
                )
                println("✅ Old app session: ${oldLoginResponse.acc_session}")

                // TODO: Store session/token or emit ViewEffect later

            } catch (e: Exception) {
                println("❌ Login failed: ${e.message}")
                // TODO: Emit error ViewEffect later
            } finally {
                _viewState.update { it.copy(isLoading = false, loginTriggered = false) }
            }
        }
    }
}