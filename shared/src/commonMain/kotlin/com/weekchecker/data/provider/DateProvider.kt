package com.weekchecker.data.provider

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class DateProvider {
    fun currentDate(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
}
