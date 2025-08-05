package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.db.SessionDatabase
import com.teoryul.mytesy.data.repo.LoginRepositoryImpl
import com.teoryul.mytesy.domain.repo.LoginRepository
import org.koin.dsl.module

val dataModule = module {
    single { SessionDatabase(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}