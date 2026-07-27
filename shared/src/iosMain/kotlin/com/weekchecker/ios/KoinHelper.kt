package com.weekchecker.ios

import com.weekchecker.di.appModule
import com.weekchecker.di.iosNotificationModule
import com.weekchecker.presentation.screen.WeekViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object KoinHelper : KoinComponent {
    val weekViewModel: WeekViewModel by inject()

    fun initKoin() {
        startKoin {
            modules(appModule, iosNotificationModule)
        }
    }
}
