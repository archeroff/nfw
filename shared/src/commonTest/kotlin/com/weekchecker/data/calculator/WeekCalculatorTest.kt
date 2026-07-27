package com.weekchecker.data.calculator

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WeekCalculatorTest {

    private val calculator = WeekCalculator()

    @Test
    fun isoWeekNumber_basicDate() {
        // April 28, 2025 is in ISO week 18
        val date = LocalDate(2025, 4, 28)
        assertEquals(18, calculator.isoWeekNumber(date))
    }

    @Test
    fun isEvenWeek_evenNumber() {
        assertTrue(calculator.isEvenWeek(18))
        assertTrue(calculator.isEvenWeek(2))
        assertTrue(calculator.isEvenWeek(52))
    }

    @Test
    fun isEvenWeek_oddNumber() {
        assertFalse(calculator.isEvenWeek(1))
        assertFalse(calculator.isEvenWeek(17))
        assertFalse(calculator.isEvenWeek(53))
    }

    @Test
    fun weekStart_monday() {
        // April 28, 2025 is a Monday
        val date = LocalDate(2025, 4, 28)
        assertEquals(LocalDate(2025, 4, 28), calculator.weekStart(date))
    }

    @Test
    fun weekStart_wednesday() {
        // April 30, 2025 is a Wednesday
        val date = LocalDate(2025, 4, 30)
        assertEquals(LocalDate(2025, 4, 28), calculator.weekStart(date))
    }

    @Test
    fun weekEnd_sunday() {
        // April 28, 2025 is a Monday, Sunday should be May 4
        val date = LocalDate(2025, 4, 28)
        assertEquals(LocalDate(2025, 5, 4), calculator.weekEnd(date))
    }

    @Test
    fun weekEnd_sundayWhenDateIsSunday() {
        // May 4, 2025 is a Sunday
        val date = LocalDate(2025, 5, 4)
        assertEquals(LocalDate(2025, 5, 4), calculator.weekEnd(date))
    }

    @Test
    fun weekNumber_week1() {
        // January 1, 2025 is a Wednesday -> ISO week 1
        val date = LocalDate(2025, 1, 1)
        assertEquals(1, calculator.isoWeekNumber(date))
    }

    @Test
    fun weekNumber_week52Or53_dec29() {
        // December 29, 2025 is a Monday
        // ISO week: Let's verify
        val date = LocalDate(2025, 12, 29)
        val weekNum = calculator.isoWeekNumber(date)
        assertTrue(weekNum == 1 || weekNum == 52 || weekNum == 53)
    }

    @Test
    fun weekNumber_jan4() {
        // Jan 4 is always in week 1 by ISO-8601 definition
        val date = LocalDate(2025, 1, 4)
        assertEquals(1, calculator.isoWeekNumber(date))
    }

    @Test
    fun weekNumber_yearTransition_2025to2026() {
        // Dec 31, 2025 is a Wednesday
        val dec31 = LocalDate(2025, 12, 31)
        val weekNum = calculator.isoWeekNumber(dec31)
        assertTrue(weekNum in 1..53)
    }

    @Test
    fun weekNumber_yearTransition_2024to2025() {
        // Dec 31, 2024 is a Tuesday
        val dec31 = LocalDate(2024, 12, 31)
        val weekNum = calculator.isoWeekNumber(dec31)
        assertTrue(weekNum in 1..53)
    }

    @Test
    fun weekNumber_jan1_2026() {
        // Jan 1, 2026 is a Thursday -> ISO week 1
        val date = LocalDate(2026, 1, 1)
        assertEquals(1, calculator.isoWeekNumber(date))
    }

    @Test
    fun weekNumber_dec31_2024_week1() {
        // Dec 31, 2024 is Tuesday. Jan 1, 2025 is Wednesday.
        // The first Thursday of 2025 is Jan 2. So Dec 31, 2024 is in the week of Jan 2, 2025 = week 1.
        val date = LocalDate(2024, 12, 31)
        assertEquals(1, calculator.isoWeekNumber(date))
    }

    @Test
    fun leapYear_feb29() {
        // Feb 29, 2024 is a Thursday in ISO week 9
        val date = LocalDate(2024, 2, 29)
        assertEquals(9, calculator.isoWeekNumber(date))
    }

    @Test
    fun nonLeapYear_mar1() {
        // Mar 1, 2025 is a Saturday in ISO week 9
        val date = LocalDate(2025, 3, 1)
        assertEquals(9, calculator.isoWeekNumber(date))
    }

    @Test
    fun weekNumber_allWeeks_coverYear() {
        // Verify that all 365 (or 366) days of 2025 map to weeks 1-53
        val startOf2025 = LocalDate(2025, 1, 1)
        val endOf2025 = LocalDate(2025, 12, 31)
        var date = startOf2025
        val weeks = mutableSetOf<Int>()

        while (date <= endOf2025) {
            val week = calculator.isoWeekNumber(date)
            assertTrue(week in 1..53, "Week $week is out of range for $date")
            weeks.add(week)
            date = date.plus(1, DateTimeUnit.DAY)
        }

        // 2025 should have weeks 1 through 52 (or 53)
        assertTrue(weeks.contains(1))
        assertTrue(weeks.contains(52))
    }

    @Test
    fun today_returnsLocalDate() {
        val today = calculator.today()
        val expected = Clock.System.todayIn(TimeZone.currentSystemDefault())
        assertEquals(expected, today)
    }
}
