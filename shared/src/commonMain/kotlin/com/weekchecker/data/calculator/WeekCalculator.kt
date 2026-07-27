package com.weekchecker.data.calculator

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class WeekCalculator {

    fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    fun isoWeekNumber(date: LocalDate): Int {
        val jan4 = LocalDate(date.year, 1, 4)
        val week1Monday = jan4.minus((jan4.dayOfWeek.ordinal).toLong(), DateTimeUnit.DAY)

        if (date < week1Monday) {
            val prevDec31 = LocalDate(date.year - 1, 12, 31)
            return isoWeekNumber(prevDec31)
        }

        val daysSinceWeek1Monday = date.toEpochDays() - week1Monday.toEpochDays()
        val weekNumber = (daysSinceWeek1Monday / 7) + 1

        if (weekNumber > 52) {
            val nextJan4 = LocalDate(date.year + 1, 1, 4)
            val nextWeek1Monday = nextJan4.minus((nextJan4.dayOfWeek.ordinal).toLong(), DateTimeUnit.DAY)
            if (date >= nextWeek1Monday) {
                return 1
            }
        }

        return weekNumber
    }

    fun isEvenWeek(weekNumber: Int): Boolean = weekNumber % 2 == 0

    fun weekStart(date: LocalDate): LocalDate {
        val daysFromMonday = date.dayOfWeek.ordinal
        return date.minus(daysFromMonday.toLong(), DateTimeUnit.DAY)
    }

    fun weekEnd(date: LocalDate): LocalDate {
        val daysToSunday = 6 - date.dayOfWeek.ordinal
        return date.plus(daysToSunday.toLong(), DateTimeUnit.DAY)
    }
}
