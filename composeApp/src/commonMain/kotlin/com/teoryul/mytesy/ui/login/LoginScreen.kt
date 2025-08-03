package com.teoryul.mytesy.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinInject()
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewState.loginTriggered) {
        if (viewState.loginTriggered) {
            viewModel.performLoginIfTriggered()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewState.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = viewModel::onLoginClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }

        if (viewState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}