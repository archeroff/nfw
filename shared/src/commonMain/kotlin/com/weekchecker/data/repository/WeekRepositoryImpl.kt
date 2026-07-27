package com.weekchecker.data.repository

import com.weekchecker.data.calculator.WeekCalculator
import com.weekchecker.domain.model.WeekInfo
import com.weekchecker.domain.repository.WeekRepository

class WeekRepositoryImpl(
    private val weekCalculator: WeekCalculator
) : WeekRepository {

    override fun getCurrentWeekInfo(): WeekInfo {
        val currentDate = weekCalculator.today()
        val weekNumber = weekCalculator.isoWeekNumber(currentDate)
        val isEven = weekCalculator.isEvenWeek(weekNumber)
        val weekStart = weekCalculator.weekStart(currentDate)
        val weekEnd = weekCalculator.weekEnd(currentDate)

        return WeekInfo(
            currentDate = currentDate,
            weekNumber = weekNumber,
            isEvenWeek = isEven,
            weekStart = weekStart,
            weekEnd = weekEnd
        )
    }
}
