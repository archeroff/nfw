package com.weekchecker.data.repository

import com.weekchecker.data.calculator.WeekCalculator
import com.weekchecker.domain.model.WeekInfo
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeekRepositoryImplTest {

    private val calculator = WeekCalculator()
    private val repository = WeekRepositoryImpl(calculator)

    @Test
    fun getCurrentWeekInfo_returnsValidData() {
        val weekInfo = repository.getCurrentWeekInfo()

        assertNotNull(weekInfo)
        assertTrue(weekInfo.weekNumber in 1..53)
        assertEquals(weekInfo.weekStart.dayOfWeek, kotlinx.datetime.DayOfWeek.MONDAY)
        assertEquals(weekInfo.weekEnd.dayOfWeek, kotlinx.datetime.DayOfWeek.SUNDAY)
    }

    @Test
    fun getCurrentWeekInfo_weekStartBeforeOrEqualCurrentDate() {
        val weekInfo = repository.getCurrentWeekInfo()
        assertTrue(weekInfo.weekStart <= weekInfo.currentDate)
    }

    @Test
    fun getCurrentWeekInfo_weekEndAfterOrEqualCurrentDate() {
        val weekInfo = repository.getCurrentWeekInfo()
        assertTrue(weekInfo.weekEnd >= weekInfo.currentDate)
    }

    @Test
    fun getCurrentWeekInfo_parityMatchesWeekNumber() {
        val weekInfo = repository.getCurrentWeekInfo()
        assertEquals(weekInfo.weekNumber % 2 == 0, weekInfo.isEvenWeek)
    }

    @Test
    fun getCurrentWeekInfo_weekSpanIs7Days() {
        val weekInfo = repository.getCurrentWeekInfo()
        val daysBetween = weekInfo.weekEnd.toEpochDays() - weekInfo.weekStart.toEpochDays()
        assertEquals(6L, daysBetween)
    }
}
