package com.weekchecker.di

import com.weekchecker.notification.NotificationScheduler
import com.weekchecker.notification.WebNotificationScheduler
import org.koin.dsl.module

val webNotificationModule = module {
    single<NotificationScheduler> {
        WebNotificationScheduler(
            weekCalculator = get()
        )
    }
}
