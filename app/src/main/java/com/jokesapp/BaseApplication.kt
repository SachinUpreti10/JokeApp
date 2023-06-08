package com.jokesapp

import android.app.Application
import com.jokesapp.di.module.apiModule
import com.jokesapp.di.module.databaseModule
import com.jokesapp.di.module.repoModule
import com.jokesapp.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(apiModule, databaseModule, repoModule, viewModelModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}