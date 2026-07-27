package com.weekchecker.domain.repository

import com.weekchecker.domain.model.WeekInfo

interface WeekRepository {
    fun getCurrentWeekInfo(): WeekInfo
}
