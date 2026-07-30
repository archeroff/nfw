package com.weekchecker.presentation.model

import com.weekchecker.domain.model.WeekInfo
import kotlinx.datetime.LocalDate

sealed class WeekUiState {
    data object Loading : WeekUiState()

    data class Success(
        val weekNumber: Int,
        val isEvenWeek: Boolean,
        val currentDate: LocalDate,
        val weekStart: LocalDate,
        val weekEnd: LocalDate,
        val nextWeekNumber: Int,
        val nextWeekIsEven: Boolean,
        val nextWeekStart: LocalDate
    ) : WeekUiState() {
        companion object {
            fun from(weekInfo: WeekInfo, nextWeekInfo: WeekInfo): Success = Success(
                weekNumber = weekInfo.weekNumber,
                isEvenWeek = weekInfo.isEvenWeek,
                currentDate = weekInfo.currentDate,
                weekStart = weekInfo.weekStart,
                weekEnd = weekInfo.weekEnd,
                nextWeekNumber = nextWeekInfo.weekNumber,
                nextWeekIsEven = nextWeekInfo.isEvenWeek,
                nextWeekStart = nextWeekInfo.weekStart
            )
        }
    }

    data class Error(val message: String) : WeekUiState()
}
