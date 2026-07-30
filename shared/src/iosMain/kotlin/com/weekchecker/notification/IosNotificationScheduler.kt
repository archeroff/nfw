package com.weekchecker.notification

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.NSDateComponents
import com.weekchecker.data.calculator.WeekCalculator

class IosNotificationScheduler(
    private val weekCalculator: WeekCalculator
) : NotificationScheduler {

    private val center = UNUserNotificationCenter.currentNotificationCenter()

    override fun scheduleWeeklyNotification() {
        center.getNotificationSettingsWithCompletionHandler { settings ->
            if (settings != null && settings.authorizationStatus == UNAuthorizationStatusAuthorized) {
                scheduleWeeklyNotificationInternal()
            }
        }
    }

    private fun scheduleWeeklyNotificationInternal() {
        center.removeAllPendingNotificationRequests()

        val content = UNMutableNotificationContent().apply {
            setTitle("W")
            setBody(buildNotificationMessage())
            setBadge(1u)
            setSound(UNNotificationSound.defaultSound)
        }

        val components = NSDateComponents().apply {
            weekday = 1
            hour = 18
            minute = 0
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            components,
            repeats = true
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "weekly_week_notification",
            content = content,
            trigger = trigger
        )

        center.addNotificationRequest(request, withCompletionHandler = null)
    }

    override fun cancelWeeklyNotification() {
        center.removeAllPendingNotificationRequests()
    }

    override fun hasPermission(): Boolean {
        var hasPermission = false
        center.getNotificationSettingsWithCompletionHandler { settings ->
            hasPermission = settings != null &&
                    settings.authorizationStatus == UNAuthorizationStatusAuthorized
        }
        return hasPermission
    }

    override fun requestPermission() {
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or
                    UNAuthorizationOptionSound or
                    UNAuthorizationOptionBadge
        ) { granted, _ ->
            if (granted) {
                scheduleWeeklyNotificationInternal()
            }
        }
    }

    override fun sendTestNotification() {
        val message = buildNotificationMessage()
        println("Notification test: $message")
    }

    private fun buildNotificationMessage(): String {
        val today = weekCalculator.today()
        val nextMonday = today.plus(
            (DayOfWeek.MONDAY.ordinal - today.dayOfWeek.ordinal + 7) % 7,
            DateTimeUnit.DAY
        )
        val nextWeekNumber = weekCalculator.isoWeekNumber(nextMonday)
        val isEven = weekCalculator.isEvenWeek(nextWeekNumber)
        val status = if (isEven) "Work From Home" else "Work From Office"
        val dateStr = formatDateLong(nextMonday)
        return "Monday $dateStr is $status!"
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
}
