package com.teoryul.mytesy.di

import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.domain.session.SessionProvider
import com.teoryul.mytesy.domain.usecase.ApplianceUseCase
import com.teoryul.mytesy.domain.usecase.LoginUseCase
import com.teoryul.mytesy.domain.usecase.RestoreSessionUseCase
import com.teoryul.mytesy.infra.AppScopes
import org.koin.dsl.module

val domainModule = module {
    single { SessionManager(get<AppScopes>().appScope, get()) }
    single<SessionProvider> { get<SessionManager>() }

    factory { LoginUseCase(get(), get(), get()) }
    factory { RestoreSessionUseCase(get(), get()) }
    single { ApplianceUseCase(get(), get()) }
}