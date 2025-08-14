package com.teoryul.mytesy.di

import com.teoryul.mytesy.util.AppLoggerDebug
import com.teoryul.mytesy.util.AppLoggerDefinition
import com.teoryul.mytesy.util.AppScopes
import org.koin.dsl.module

val utilModule = module {
    // TODO Change for release builds
    single<AppLoggerDefinition> { AppLoggerDebug() }
    //single<AppLoggerDefinition> { AppLoggerRelease() }

    single { AppScopes() }
}