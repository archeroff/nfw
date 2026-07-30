package com.weekchecker.domain.usecase

import com.weekchecker.domain.model.WeekInfo
import com.weekchecker.domain.repository.WeekRepository
import kotlinx.datetime.LocalDate

class GetCurrentWeekUseCase(
    private val repository: WeekRepository
) {
    operator fun invoke(): WeekInfo = repository.getCurrentWeekInfo()
    operator fun invoke(date: LocalDate): WeekInfo = repository.getWeekInfoFor(date)
}
