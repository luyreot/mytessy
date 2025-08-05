package com.teoryul.mytesy

import android.app.Application
import com.teoryul.mytesy.di.KoinInitializer.initKoin
import org.koin.android.ext.koin.androidContext

class MyTesyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            appDeclaration = {
                androidContext(this@MyTesyApp)
            }
        )
    }
}