package com.weekchecker.di

import com.weekchecker.notification.IosNotificationScheduler
import com.weekchecker.notification.NotificationScheduler
import org.koin.dsl.module

val iosNotificationModule = module {
    single<NotificationScheduler> {
        IosNotificationScheduler(
            weekCalculator = get()
        )
    }
}
