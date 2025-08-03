package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { LoginRepository() }
}