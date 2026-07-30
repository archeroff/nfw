package com.weekchecker.presentation.screen

import androidx.lifecycle.ViewModel
import com.weekchecker.domain.usecase.GetCurrentWeekUseCase
import com.weekchecker.notification.NotificationScheduler
import com.weekchecker.presentation.model.WeekUiState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeekViewModel(
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeekUiState>(WeekUiState.Loading)
    val uiState: StateFlow<WeekUiState> = _uiState.asStateFlow()

    init {
        refresh()
        setupNotification()
    }

    fun refresh() {
        selectDate(null)
    }

    fun selectDate(date: LocalDate?) {
        try {
            val weekInfo = if (date != null) {
                getCurrentWeekUseCase(date)
            } else {
                getCurrentWeekUseCase()
            }
            val nextMonday = weekInfo.weekStart.plus(7, DateTimeUnit.DAY)
            val nextWeekInfo = getCurrentWeekUseCase(nextMonday)
            _uiState.value = WeekUiState.Success.from(weekInfo, nextWeekInfo)
        } catch (e: Exception) {
            _uiState.value = WeekUiState.Error(
                e.message ?: "An unexpected error occurred"
            )
        }
    }

    private fun setupNotification() {
        try {
            if (notificationScheduler.hasPermission()) {
                notificationScheduler.scheduleWeeklyNotification()
            } else {
                notificationScheduler.requestPermission()
            }
        } catch (_: Exception) {
            // Notifications are optional - don't crash
        }
    }
}
