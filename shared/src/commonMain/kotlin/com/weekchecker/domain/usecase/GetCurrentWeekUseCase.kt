package com.weekchecker.domain.usecase

import com.weekchecker.domain.model.WeekInfo
import com.weekchecker.domain.repository.WeekRepository

class GetCurrentWeekUseCase(
    private val repository: WeekRepository
) {
    operator fun invoke(): WeekInfo = repository.getCurrentWeekInfo()
}
