package com.teoryul.mytesy.di

import com.teoryul.mytesy.domain.usecase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}