package com.teoryul.mytesy.di

import com.teoryul.mytesy.ui.login.LoginViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { LoginViewModel(get()) }
}