package com.teoryul.mytesy.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.domain.usecase.ApplianceGroupItem
import com.teoryul.mytesy.ui.addappliance.AddApplianceScreen
import com.teoryul.mytesy.ui.appliancedetail.ApplianceDetailScreen
import com.teoryul.mytesy.ui.appliancegroup.ApplianceGroupScreen
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
                Screen.Home -> CenterAlignedTopAppBar(title = { Text("Dashboard") })

                Screen.AddAppliance -> CenterAlignedTopAppBar(title = { Text("First steps") })

                is Screen.ApplianceDetail -> {
                    val title = currentScreen.appliance.shortName
                        ?.takeIf { it.isNotBlank() }
                        ?: "Modeco II" // TODO: Appliances endpoint does not return this string. Find from where can you get it.
                    CenterAlignedTopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clickable(onClick = { navigateBack() }),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Back"
                            )
                        }
                    )
                }

                Screen.AddApplianceChooseGroup,
                Screen.AddApplianceGroupConvectors,
                Screen.AddApplianceGroupWaterHeaters -> {
                    val title = when (currentScreen) {
                        Screen.AddApplianceChooseGroup -> "Choose group"
                        Screen.AddApplianceGroupConvectors -> "Choose convector"
                        Screen.AddApplianceGroupWaterHeaters -> "Choose electric water heater"
                        else -> "Choose"
                    }
                    CenterAlignedTopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clickable(onClick = { navigateBack() }),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Back"
                            )
                        }
                    )
                }

                Screen.Notifications -> CenterAlignedTopAppBar(title = { Text("Notifications") })

                Screen.Settings -> CenterAlignedTopAppBar(title = { Text("Settings") })

                Screen.ComingSoon -> CenterAlignedTopAppBar(
                    title = {},
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable(onClick = { navigateBack() }),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back"
                        )
                    }
                )

                else -> {}
            }
        },
        bottomBar = {
            if (currentScreen in BottomNavItem.items.map { it.screen }) {
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
                        Screen.Welcome -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            WelcomeScreen(
                                onSignUpClick = { navigateTo(Screen.ComingSoon) },
                                onSignInClick = { navigateTo(Screen.Login) },
                                onLanguageClick = { navigateTo(Screen.ComingSoon) },
                                onSessionRestored = { navigateToRoot(Screen.Home) }
                            )
                        }

                        Screen.Login -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            LoginScreen(
                                onForgotPasswordClick = { navigateTo(Screen.ComingSoon) },
                                onBackClick = { navigateBack() },
                                onLoginSuccess = { navigateToRoot(Screen.Home) }
                            )
                        }

                        Screen.ComingSoon -> WithScreenViewModelStore(key = viewModelStoreKey) {
                            ComingSoonScreen(onBackClick = { navigateBack() })
                        }

                        Screen.Home -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                HomeScreen(
                                    stateHolderKey = stateHolderKey,
                                    onAddApplianceClick = { navigateTo(Screen.AddAppliance) },
                                    onApplianceClick = { applianceEntity ->
                                        navigateTo(Screen.ApplianceDetail(applianceEntity))
                                    }
                                )
                            }
                        }

                        is Screen.ApplianceDetail -> stateHolder.SaveableStateProvider(
                            stateHolderKey
                        ) {
                            // TODO
                            // do not use WithRetainedTabViewModelStore for nested screens
                            // to avoid going back to the home screen on screen rotation changes
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                ApplianceDetailScreen(
                                    stateHolderKey = stateHolderKey,
                                    applianceId = screen.appliance.id.orEmpty(),
                                    navigateToComingSoon = { navigateTo(Screen.ComingSoon) }
                                )
                            }
                        }

                        Screen.AddAppliance -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                AddApplianceScreen(
                                    stateHolderKey = stateHolderKey,
                                    onNextClick = { navigateTo(Screen.AddApplianceChooseGroup) }
                                )
                            }
                        }

                        Screen.Notifications -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                NotificationsScreen(stateHolderKey = stateHolderKey)
                            }
                        }

                        Screen.Settings -> stateHolder.SaveableStateProvider(stateHolderKey) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                SettingsScreen(
                                    stateHolderKey = stateHolderKey,
                                    navigateToComingSoon = { navigateTo(Screen.ComingSoon) }
                                )
                            }
                        }

                        Screen.AddApplianceChooseGroup -> stateHolder.SaveableStateProvider(
                            stateHolderKey
                        ) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                ApplianceGroupScreen(
                                    stateHolderKey = stateHolderKey,
                                    items = viewModel.applianceGroups.commonGroupScreen,
                                    onItemClick = { item ->
                                        if (item == ApplianceGroupItem.Convectors) {
                                            navigateTo(Screen.AddApplianceGroupConvectors)
                                            return@ApplianceGroupScreen
                                        }
                                        if (item == ApplianceGroupItem.WaterHeaters) {
                                            navigateTo(Screen.AddApplianceGroupWaterHeaters)
                                            return@ApplianceGroupScreen
                                        }
                                    }
                                )
                            }
                        }

                        Screen.AddApplianceGroupConvectors -> stateHolder.SaveableStateProvider(
                            stateHolderKey
                        ) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                ApplianceGroupScreen(
                                    stateHolderKey = stateHolderKey,
                                    items = viewModel.applianceGroups.convectorScreen,
                                    onItemClick = { item ->
                                        navigateTo(Screen.ComingSoon)
                                    }
                                )
                            }
                        }

                        Screen.AddApplianceGroupWaterHeaters -> stateHolder.SaveableStateProvider(
                            stateHolderKey
                        ) {
                            WithRetainedTabViewModelStore(key = viewModelStoreKey) {
                                ApplianceGroupScreen(
                                    stateHolderKey = stateHolderKey,
                                    items = viewModel.applianceGroups.waterHeaterScreen,
                                    onItemClick = { item ->
                                        navigateTo(Screen.ComingSoon)
                                    }
                                )
                            }
                        }

                        Screen.ConvertorConvEcoCN04 -> TODO()
                        Screen.ConvertorFinEcoCN06 -> TODO()
                        Screen.ConvertorFloorEcoCN052 -> TODO()
                        Screen.ConvertorHeatEcoCN03 -> TODO()
                        Screen.ConvertorHeatEcoCN031 -> TODO()
                        Screen.ConvertorLivEcoCN051 -> TODO()
                        Screen.WaterHeaterBelliSlimoE31 -> TODO()
                        Screen.WaterHeaterBelliSlimoLiteCloudE32 -> TODO()
                        Screen.WaterHeaterBiLightCloudB15 -> TODO()
                        Screen.WaterHeaterModEcoC21 -> TODO()
                        Screen.WaterHeaterModEcoC22 -> TODO()
                    }
                }
            }
        }
    )
}