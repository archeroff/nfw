package com.weekchecker.domain.model

import kotlinx.datetime.LocalDate

data class WeekInfo(
    val currentDate: LocalDate,
    val weekNumber: Int,
    val isEvenWeek: Boolean,
    val weekStart: LocalDate,
    val weekEnd: LocalDate
) {
    val statusText: String
        get() = if (isEvenWeek) "Even Week" else "Odd Week"

    val statusTextFr: String
        get() = if (isEvenWeek) "Semaine paire" else "Semaine impaire"
}
