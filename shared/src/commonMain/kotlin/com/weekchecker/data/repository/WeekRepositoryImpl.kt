package com.weekchecker.data.repository

import com.weekchecker.data.calculator.WeekCalculator
import com.weekchecker.domain.model.WeekInfo
import com.weekchecker.domain.repository.WeekRepository
import kotlinx.datetime.LocalDate

class WeekRepositoryImpl(
    private val weekCalculator: WeekCalculator
) : WeekRepository {

    override fun getCurrentWeekInfo(): WeekInfo = weekInfoFor(weekCalculator.today())

    override fun getWeekInfoFor(date: LocalDate): WeekInfo = weekInfoFor(date)

    private fun weekInfoFor(date: LocalDate): WeekInfo {
        val weekNumber = weekCalculator.isoWeekNumber(date)
        val isEven = weekCalculator.isEvenWeek(weekNumber)
        val weekStart = weekCalculator.weekStart(date)
        val weekEnd = weekCalculator.weekEnd(date)

        return WeekInfo(
            currentDate = date,
            weekNumber = weekNumber,
            isEvenWeek = isEven,
            weekStart = weekStart,
            weekEnd = weekEnd
        )
    }
}
