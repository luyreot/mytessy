package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.repo.LoginRepositoryImpl
import com.teoryul.mytesy.domain.repo.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl() }
}