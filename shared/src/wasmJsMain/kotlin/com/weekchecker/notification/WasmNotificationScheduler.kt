package com.weekchecker.notification

import com.weekchecker.data.calculator.WeekCalculator
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

@JsFun(
    """
    () => typeof Notification !== 'undefined' && Notification.permission === 'granted'
    """
)
internal external fun hasNotificationPermission(): Boolean

@JsFun(
    """
    (title, body) => {
        if (typeof Notification === 'undefined' || Notification.permission !== 'granted') return false;
        try {
            new Notification(title, { body: body, tag: 'week-checker' });
            return true;
        } catch (e) {
            return false;
        }
    }
    """
)
internal external fun showNotification(title: String, body: String): Boolean

class WebNotificationScheduler(
    private val weekCalculator: WeekCalculator
) : NotificationScheduler {

    override fun scheduleWeeklyNotification() {
        // No background scheduling on the web without a push server.
    }

    override fun cancelWeeklyNotification() {
    }

    override fun hasPermission(): Boolean =
        try {
            hasNotificationPermission()
        } catch (_: Throwable) {
            false
        }

    override fun requestPermission() {
        // The Web Notifications API only allows prompting from a user gesture.
    }

    override fun sendTestNotification() {
        try {
            if (!hasPermission()) return
            val today = weekCalculator.today()
            val nextMonday = today.plus(
                (DayOfWeek.MONDAY.ordinal - today.dayOfWeek.ordinal + 7) % 7,
                DateTimeUnit.DAY
            )
            val weekNumber = weekCalculator.isoWeekNumber(nextMonday)
            val isEven = weekCalculator.isEvenWeek(weekNumber)
            val status = if (isEven) "Work From Home" else "Work From Office"
            showNotification("W", "Monday ${formatDate(nextMonday)} is $status!")
        } catch (_: Throwable) {
            // Notifications are optional - don't crash.
        }
    }

    private fun formatDate(date: kotlinx.datetime.LocalDate): String {
        val month = when (date.monthNumber) {
            1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
            5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
            9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
            else -> ""
        }
        return "${date.dayOfMonth} $month ${date.year}"
    }
}
