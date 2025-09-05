package com.teoryul.mytesy.di

import com.teoryul.mytesy.ui.main.AppMainViewModel
import com.teoryul.mytesy.ui.addappliance.AddApplianceViewModel
import com.teoryul.mytesy.ui.home.HomeViewModel
import com.teoryul.mytesy.ui.login.LoginViewModel
import com.teoryul.mytesy.ui.notifications.NotificationsViewModel
import com.teoryul.mytesy.ui.settings.SettingsViewModel
import com.teoryul.mytesy.ui.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AppMainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddApplianceViewModel)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::SettingsViewModel)
}