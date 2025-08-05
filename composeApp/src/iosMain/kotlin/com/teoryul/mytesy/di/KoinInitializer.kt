package com.teoryul.mytesy.di

fun InitKoin() {
    KoinInitializer.initKoin(
        appModule = appModule
    )
}