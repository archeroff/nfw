package com.weekchecker

import android.app.Application
import com.weekchecker.di.appModule
import com.weekchecker.di.androidNotificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeekCheckerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WeekCheckerApp)
            modules(appModule, androidNotificationModule)
        }
    }
}
