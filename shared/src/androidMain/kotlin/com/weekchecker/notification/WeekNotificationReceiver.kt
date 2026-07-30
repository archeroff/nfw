package com.weekchecker.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.weekchecker.data.calculator.WeekCalculator

class WeekNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(AndroidNotificationScheduler.EXTRA_TITLE) ?: "W"
        val message = intent.getStringExtra(AndroidNotificationScheduler.EXTRA_MESSAGE) ?: "Check your week type!"

        val notificationScheduler = AndroidNotificationScheduler(context, WeekCalculator())
        notificationScheduler.showNotification(title, message)
    }
}
