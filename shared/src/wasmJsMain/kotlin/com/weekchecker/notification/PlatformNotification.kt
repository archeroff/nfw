package com.weekchecker.notification

import com.weekchecker.data.calculator.WeekCalculator

actual fun createNotificationScheduler(): NotificationScheduler =
    WebNotificationScheduler(weekCalculator = WeekCalculator())
