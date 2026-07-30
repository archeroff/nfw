package com.weekchecker.notification

interface NotificationScheduler {
    fun scheduleWeeklyNotification()
    fun cancelWeeklyNotification()
    fun hasPermission(): Boolean
    fun requestPermission()
    fun sendTestNotification()
}
