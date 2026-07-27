package com.weekchecker.notification

actual fun createNotificationScheduler(): NotificationScheduler = IosNotificationScheduler(
    com.weekchecker.data.calculator.WeekCalculator()
)
