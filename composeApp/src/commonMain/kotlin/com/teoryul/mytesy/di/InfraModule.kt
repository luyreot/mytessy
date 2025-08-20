package com.teoryul.mytesy.di

import com.teoryul.mytesy.infra.AppLoggerDebug
import com.teoryul.mytesy.infra.AppLoggerDefinition
import com.teoryul.mytesy.infra.AppScopes
import com.teoryul.mytesy.infra.polling.PollingManager
import org.koin.dsl.module

val infraModule = module {
    // TODO Change for release builds
    single<AppLoggerDefinition> { AppLoggerDebug() }
    //single<AppLoggerDefinition> { AppLoggerRelease() }

    single { AppScopes() }

    single { PollingManager(get<AppScopes>().appScope) }
}