package com.weekchecker.presentation.screen

import androidx.lifecycle.ViewModel
import com.weekchecker.domain.usecase.GetCurrentWeekUseCase
import com.weekchecker.notification.NotificationScheduler
import com.weekchecker.presentation.model.WeekUiState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
        try {
            val weekInfo = getCurrentWeekUseCase()
            val now = Clock.System.now()
            val time = now.toLocalDateTime(TimeZone.currentSystemDefault()).let {
                "%02d:%02d".format(it.hour, it.minute)
            }
            _uiState.value = WeekUiState.Success.from(weekInfo, time)
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
