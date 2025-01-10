package com.al4apps.mycomposeapp

import android.app.Application
import com.al4apps.mycomposeapp.di.appModule
import com.al4apps.mycomposeapp.di.dataModule
import com.al4apps.mycomposeapp.di.dbModule
import com.al4apps.mycomposeapp.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@App)
            modules(appModule, dbModule, dataModule, useCasesModule)
        }
    }
}