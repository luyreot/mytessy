package com.teoryul.mytesy.di

import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.domain.usecase.LoginUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SessionManager() }
    factory { LoginUseCase(get(), get()) }
}