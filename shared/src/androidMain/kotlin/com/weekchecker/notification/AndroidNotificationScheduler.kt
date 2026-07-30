package com.weekchecker.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.weekchecker.data.calculator.WeekCalculator
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus
import kotlinx.datetime.LocalDate
import java.util.Calendar

class AndroidNotificationScheduler(
    private val context: Context,
    private val weekCalculator: WeekCalculator
) : NotificationScheduler {

    companion object {
        const val CHANNEL_ID = "week_checker_channel"
        const val NOTIFICATION_ID = 1001
        const val ALARM_REQUEST_CODE = 2001
        const val EXTRA_TITLE = "notification_title"
        const val EXTRA_MESSAGE = "notification_message"
    }

    override fun scheduleWeeklyNotification() {
        createNotificationChannel()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WeekNotificationReceiver::class.java).apply {
            putExtra(EXTRA_TITLE, "Week Checker")
            putExtra(EXTRA_MESSAGE, buildNotificationMessage())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    override fun cancelWeeklyNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WeekNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val activity = context as? android.app.Activity
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Weekly Week Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifies you every Sunday at 6pm about next week's type"
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    internal fun buildNotificationMessage(): String {
        val today = weekCalculator.today()
        val nextMonday = today.plus(
            (DayOfWeek.MONDAY.ordinal - today.dayOfWeek.ordinal + 7) % 7,
            DateTimeUnit.DAY
        )
        val nextWeekNumber = weekCalculator.isoWeekNumber(nextMonday)
        val isEven = weekCalculator.isEvenWeek(nextWeekNumber)
        val status = if (isEven) "Work From Home" else "Work From Office"
        val dateStr = formatDateLong(nextMonday)
        return "Tomorrow Monday $dateStr is $status!"
    }

    private fun formatDateLong(date: kotlinx.datetime.LocalDate): String {
        val month = when (date.monthNumber) {
            1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
            5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
            9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
            else -> ""
        }
        return "${date.dayOfMonth} $month ${date.year}"
    }

    override fun sendTestNotification() {
        val message = buildNotificationMessage()
        showNotification("Week Checker (Test)", message)
    }

    fun showNotification(title: String, message: String) {
        createNotificationChannel()

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (hasPermission()) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }
    }
}
