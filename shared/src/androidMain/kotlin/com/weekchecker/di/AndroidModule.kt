package com.weekchecker.di

import com.weekchecker.data.calculator.WeekCalculator
import com.weekchecker.notification.AndroidNotificationScheduler
import com.weekchecker.notification.NotificationScheduler
import org.koin.dsl.module

val androidNotificationModule = module {
    single<NotificationScheduler> {
        AndroidNotificationScheduler(
            context = get(),
            weekCalculator = get()
        )
    }
}
