package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.db.DatabaseDriverFactory
import com.teoryul.mytesy.util.AppVisibilityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory { DatabaseDriverFactory(androidContext()) }
    single { AppVisibilityProvider() }
}