package com.teoryul.mytesy.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teoryul.mytesy.ui.common.RegisterBackBlocker
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onForgotPasswordClick: () -> Unit,
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewState.loginTriggered) {
        if (viewState.loginTriggered) {
            viewModel.performLoginIfTriggered()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { effect ->
            when (effect) {
                is LoginViewEffect.NavigateToHome -> onLoginSuccess()
            }
        }
    }

    RegisterBackBlocker(viewState.isLoading)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (!viewState.isLoading) onBackClick() },
                    enabled = !viewState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Log in",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                OutlinedTextField(
                    value = viewState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("E-mail", color = Color(0xFF5E006C)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = viewState.emailError != null,
                    supportingText = {
                        viewState.emailError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    enabled = !viewState.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewState.password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Password", color = Color(0xFF5E006C)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !viewState.isLoading,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Forgot your password?",
                    color = Color(0xFF5E006C),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable(enabled = !viewState.isLoading) {
                            if (!viewState.isLoading) onForgotPasswordClick()
                        }
                )

                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = viewState.errorMessage != null,
                    enter = fadeIn(tween(durationMillis = 100)) +
                            expandVertically(tween(durationMillis = 100)),
                    exit = fadeOut(tween(durationMillis = 100)) +
                            shrinkVertically(tween(durationMillis = 100))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFC107))
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = viewState.errorMessage.orEmpty(),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Button(
                    onClick = {
                        if (viewState.isFormValid && !viewState.isLoading) {
                            viewModel.onLoginClicked()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    enabled = viewState.isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5E006C),
                        contentColor = Color.White
                    )
                ) {
                    if (viewState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Log in", color = Color.White)
                    }
                }

                AnimatedVisibility(
                    visible = viewState.isLoading && viewState.showDelayedLoadingMessage,
                    enter = fadeIn(tween(150)),
                    exit = fadeOut(tween(150))
                ) {
                    Text(
                        text = "Login is taking longer than usual… Please wait.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}