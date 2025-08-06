package com.teoryul.mytesy.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.ui.comingsoon.ComingSoonScreen
import com.teoryul.mytesy.ui.login.LoginScreen
import com.teoryul.mytesy.ui.welcome.WelcomeScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AppNavigation() {
    var screenBackStack by rememberSaveable(stateSaver = ScreenBackStackSaver) {
        mutableStateOf(listOf(Screen.Welcome))
    }

    val currentScreen = screenBackStack.last()
    var isNavigatingBack by remember { mutableStateOf(false) }

    fun navigateTo(screen: Screen) {
        isNavigatingBack = false
        screenBackStack = screenBackStack + screen
    }

    fun navigateBack() {
        if (screenBackStack.size > 1) {
            isNavigatingBack = true
            screenBackStack = screenBackStack.dropLast(1)
        }
    }

    BackHandlerPlatform(
        enabled = screenBackStack.size > 1,
        onBack = { navigateBack() }
    )

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (isNavigatingBack) {
                        slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    }.using(SizeTransform(clip = false))
                }
            ) { screen ->
                when (screen) {
                    is Screen.Welcome -> WelcomeScreen(
                        onSignUpClick = { navigateTo(Screen.ComingSoon) },
                        onSignInClick = { navigateTo(Screen.Login) },
                        onLanguageClick = { navigateTo(Screen.ComingSoon) }
                    )

                    is Screen.Login -> LoginScreen(
                        onForgotPasswordClick = { navigateTo(Screen.ComingSoon) },
                        onBackClick = { navigateBack() }
                    )

                    is Screen.ComingSoon -> ComingSoonScreen(
                        onBackClick = { navigateBack() }
                    )
                }
            }
        }
    }
}