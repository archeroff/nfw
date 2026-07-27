package com.weekchecker.notification

actual fun createNotificationScheduler(): NotificationScheduler {
    throw UnsupportedOperationException(
        "AndroidNotificationScheduler must be created with Android context. Use createNotificationScheduler(context, weekCalculator) instead."
    )
}

fun createNotificationScheduler(
    context: android.content.Context,
    weekCalculator: com.weekchecker.data.calculator.WeekCalculator
): NotificationScheduler = AndroidNotificationScheduler(context, weekCalculator)
