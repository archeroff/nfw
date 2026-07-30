package com.weekchecker.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.weekchecker.presentation.model.WeekUiState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime

@Composable
fun WeekCheckerScreen(viewModel: WeekViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val currentState = state) {
                is WeekUiState.Loading -> LoadingContent()
                is WeekUiState.Success -> WeekContent(
                    state = currentState,
                    onDateSelected = { viewModel.selectDate(it) }
                )
                is WeekUiState.Error -> ErrorContent(
                    message = currentState.message,
                    onRetry = { viewModel.refresh() }
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(48.dp)
            .semantics { contentDescription = "Loading week information" }
    )
}

private fun weekColor(isEven: Boolean): Color =
    if (isEven) Color(0xFF2E7D32) else Color(0xFFC62828)

private fun weekBgColor(isEven: Boolean): Color =
    if (isEven) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeekContent(
    state: WeekUiState.Success,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate = state.currentDate
    val accentColor = weekColor(state.isEvenWeek)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Week Checker",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.semantics {
                contentDescription = "Week Checker application title"
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeekInfoCard(state = state, accentColor = accentColor)

        Spacer(modifier = Modifier.height(16.dp))

        WeekCalendar(weekStart = state.weekStart, accentColor = accentColor)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics { contentDescription = "Pick a date to check its week" }
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pick a date: ${formatDate(selectedDate)}",
                style = MaterialTheme.typography.titleMedium,
                color = accentColor
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        NextWeekCard(state = state)

    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toEpochDays() * 86400000L
        )

        val datePickerColors = DatePickerDefaults.colors(
            selectedDayContainerColor = accentColor,
            selectedDayContentColor = Color.White,
            todayContentColor = accentColor,
            todayDateBorderColor = accentColor,
            navigationContentColor = accentColor,
            subheadContentColor = accentColor,
            yearContentColor = accentColor,
            currentYearContentColor = Color.White,
            selectedYearContainerColor = accentColor,
            weekdayContentColor = accentColor,
            dayContentColor = Color(0xFF1A1C19)
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val date = instant.toLocalDateTime(TimeZone.UTC).date
                        onDateSelected(date)
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = accentColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = accentColor)
                }
            },
            colors = datePickerColors
        ) {
            DatePicker(state = datePickerState, colors = datePickerColors)
        }
    }
}

@Composable
private fun WeekInfoCard(state: WeekUiState.Success, accentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "Week ${state.weekNumber}, ${if (state.isEvenWeek) "Work From Home" else "Work From Office"}"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = formatDate(state.currentDate),
                style = MaterialTheme.typography.bodyLarge,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Week ${state.weekNumber}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            StatusChip(isEven = state.isEvenWeek)
        }
    }
}

@Composable
private fun NextWeekCard(state: WeekUiState.Success) {
    val nextAccent = weekColor(state.nextWeekIsEven)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "Next week ${state.nextWeekNumber}, ${if (state.nextWeekIsEven) "Work From Home" else "Work From Office"}"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = formatDate(state.nextWeekStart),
                style = MaterialTheme.typography.bodyLarge,
                color = nextAccent
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Week ${state.nextWeekNumber}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = nextAccent
            )

            StatusChip(isEven = state.nextWeekIsEven)
        }
    }
}

@Composable
private fun StatusChip(isEven: Boolean) {
    Card(
        colors = CardDefaults.cardColors(containerColor = weekBgColor(isEven)),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = if (isEven) "Work From Home" else "Work From Office",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = weekColor(isEven)
        )
    }
}

private val neutralColor = Color(0xFF1A1C19)

private val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
private fun WeekCalendar(weekStart: LocalDate, accentColor: Color) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0..6) {
            val day = weekStart.plus(i.toLong(), DateTimeUnit.DAY)
            val isToday = day == today
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "${dayLabels[i]} ${day.dayOfMonth}"
                }
            ) {
                Text(
                    text = dayLabels[i],
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isToday) accentColor else neutralColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = day.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (isToday) accentColor else neutralColor
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val month = when (date.monthNumber) {
        1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"
        5 -> "May"; 6 -> "Jun"; 7 -> "Jul"; 8 -> "Aug"
        9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
        else -> ""
    }
    return "${date.dayOfMonth} $month ${date.year}"
}
