package com.example.explorersos


import android.app.Application
import com.example.explorersos.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ExplorerSOSApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ExplorerSOSApp) // Provide Android context
            modules(appModule)
        }
    }
}