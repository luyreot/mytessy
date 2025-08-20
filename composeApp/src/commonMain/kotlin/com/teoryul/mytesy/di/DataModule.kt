package com.teoryul.mytesy.di

import com.teoryul.mytesy.data.api.ApiClient
import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.data.api.plugin.AuthGuardPlugin
import com.teoryul.mytesy.data.common.ErrorResultMapperImpl
import com.teoryul.mytesy.data.db.ApplianceTable
import com.teoryul.mytesy.data.db.SessionTableImpl
import com.teoryul.mytesy.data.repo.ApplianceRepositoryImpl
import com.teoryul.mytesy.domain.model.ErrorResultMapper
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.domain.session.SessionTable
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single { AuthGuardPlugin(get()) }
    single { ApiClient(get(), get()) }
    single { ApiService(get(), get()) }

    single<ErrorResultMapper> { ErrorResultMapperImpl() }

    single<SessionTable> { SessionTableImpl(get()) }
    single { ApplianceTable(get()) }

    single<ApplianceRepository> { ApplianceRepositoryImpl(get(), get()) }
}