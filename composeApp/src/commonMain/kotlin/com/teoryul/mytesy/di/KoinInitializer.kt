package com.teoryul.mytesy.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

object KoinInitializer {

    fun initKoin(
        appDeclaration: KoinAppDeclaration = {},
        appModule: Module? = null
    ) {
        startKoin {
            appDeclaration()
            modules(
                buildList {
                    appModule?.let { add(it) }
                    add(utilModule)
                    add(dataModule)
                    add(domainModule)
                    add(viewModelModule)
                }
            )
        }
    }
}