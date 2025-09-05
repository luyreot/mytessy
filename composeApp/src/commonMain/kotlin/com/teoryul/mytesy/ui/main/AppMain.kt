package com.teoryul.mytesy.ui.main

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.ui.addappliance.AddApplianceScreen
import com.teoryul.mytesy.ui.comingsoon.ComingSoonScreen
import com.teoryul.mytesy.ui.common.AlertDialog
import com.teoryul.mytesy.ui.home.HomeScreen
import com.teoryul.mytesy.ui.lifecycle.WithRetainedTabViewModelStore
import com.teoryul.mytesy.ui.lifecycle.WithScreenViewModelStore
import com.teoryul.mytesy.ui.login.LoginScreen
import com.teoryul.mytesy.ui.navigation.BackHandlerPlatform
import com.teoryul.mytesy.ui.navigation.BottomNavItem
import com.teoryul.mytesy.ui.navigation.BottomNavigationBar
import com.teoryul.mytesy.ui.navigation.Screen
import com.teoryul.mytesy.ui.navigation.ScreenBackStackSaver
import com.teoryul.mytesy.ui.notifications.NotificationsScreen
import com.teoryul.mytesy.ui.settings.SettingsScreen
import com.teoryul.mytesy.ui.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun AppMain(
    viewModel: AppMainViewModel = koinViewModel()
) {
    val stateHolder = rememberSaveableStateHolder()

    var screenBackStack by rememberSaveable(stateSaver = ScreenBackStackSaver) {
        mutableStateOf(listOf<Screen>(Screen.Welcome))
    }

    val currentScreen = screenBackStack.last()
    var isNavigatingBack by rememberSaveable { mutableStateOf(false) }

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

    fun navigateToRoot(screen: Screen) {
        isNavigatingBack = false
        screenBackStack = listOf(screen)
    }

    fun navigatePopUpToInclusive(screen: Screen) {
        val index = screenBackStack.indexOfFirst { it::class == screen::class }

        screenBackStack = if (index >= 0) {
            // Remove all screens up to and including the target
            screenBackStack.subList(0, index) + screen
        } else {
            // Not in backstack — just add it
            screenBackStack + screen
        }

        isNavigatingBack = false
    }

    var showSessionExpiredDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { effect ->
            when (effect) {
                is AppMainViewEffect.SessionLost -> {
                    showSessionExpiredDialog = true
                    navigateToRoot(Screen.Welcome)
                }
            }
        }
    }

    AlertDialog(
        show = showSessionExpiredDialog,
        title = "Your session expired",
        message = "Please sign in to continue.",
        buttonText = "Sign in",
        onDismiss = {
            showSessionExpiredDialog = false
            navigateTo(Screen.Login)
        }
    )

    BackHandlerPlatform(
        enabled = screenBackStack.size > 1,
        onBack = { navigateBack() }
    )

    Scaffold(
        topBar = {
            when (currentScreen) {
                is Screen.Home -> CenterAlignedTopAppBar(
                    title = { Text("Dashboard") }
                )

                is Screen.AddAppliance -> CenterAlignedTopAppBar(
                    title = { Text("First steps") }
                )

                is Screen.Notifications -> CenterAlignedTopAppBar(
                    title = { Text("Notifications") }
                )

                is Screen.Settings -> CenterAlignedTopAppBar(
                    title = { Text("Settings") }
                )

                else -> {}
            }
        },
        bottomBar = {
            if (currentScreen in BottomNavItem.Companion.items.map { it.screen }) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onTabSelected = { tab ->
                        navigatePopUpToInclusive(tab.screen)
                    }
                )
            }
        },
        content = { innerPadding ->
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
                    val viewModelStoreKey: String = screen::class.simpleName.toString()
                    val stateHolderKey = "tab:$viewModelStoreKey"
                    when (screen) {
                        is Screen.Welcome -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            WelcomeScreen(
                                onSignUpClick = { navigateTo(Screen.ComingSoon) },
                                onSignInClick = { navigateTo(Screen.Login) },
                                onLanguageClick = { navigateTo(Screen.ComingSoon) },
                                onSessionRestored = { navigateToRoot(Screen.Home) }
                            )
                        }

                        is Screen.Login -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            LoginScreen(
                                onForgotPasswordClick = { navigateTo(Screen.ComingSoon) },
                                onBackClick = { navigateBack() },
                                onLoginSuccess = { navigateToRoot(Screen.Home) }
                            )
                        }

                        is Screen.ComingSoon -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            ComingSoonScreen(
                                onBackClick = { navigateBack() }
                            )
                        }

                        is Screen.Home -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                HomeScreen(
                                    stateHolderKey = stateHolderKey,
                                    onAddApplianceClick = { navigateTo(Screen.AddAppliance) },
                                    onApplianceClick = { applianceEntity ->
                                        // TODO: Implement appliance detail screen
                                    }
                                )
                            }
                        }

                        is Screen.AddAppliance -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                AddApplianceScreen(
                                    stateHolderKey = stateHolderKey
                                )
                            }
                        }

                        is Screen.Notifications -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                NotificationsScreen(
                                    stateHolderKey = stateHolderKey
                                )
                            }
                        }

                        is Screen.Settings -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                SettingsScreen(
                                    stateHolderKey = stateHolderKey
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}