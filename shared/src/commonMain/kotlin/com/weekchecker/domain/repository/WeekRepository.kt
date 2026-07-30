package com.weekchecker.domain.repository

import com.weekchecker.domain.model.WeekInfo
import kotlinx.datetime.LocalDate

interface WeekRepository {
    fun getCurrentWeekInfo(): WeekInfo
    fun getWeekInfoFor(date: LocalDate): WeekInfo
}
