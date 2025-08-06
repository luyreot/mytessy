package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.api.ApiClient
import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.data.db.ApplianceTable
import com.teoryul.mytesy.data.db.SessionTableImpl
import com.teoryul.mytesy.data.repo.ApplianceRepositoryImpl
import com.teoryul.mytesy.data.repo.LoginRepositoryImpl
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.domain.repo.LoginRepository
import com.teoryul.mytesy.domain.session.SessionTable
import org.koin.dsl.module

val dataModule = module {
    single { ApiClient() }
    single { ApiService(get()) }

    single<SessionTable> { SessionTableImpl(get()) }
    single { ApplianceTable(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    single<ApplianceRepository> { ApplianceRepositoryImpl(get(), get()) }
}