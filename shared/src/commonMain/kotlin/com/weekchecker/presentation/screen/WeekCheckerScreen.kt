package com.weekchecker.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weekchecker.presentation.model.WeekUiState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

@Composable
fun WeekCheckerScreen(viewModel: WeekViewModel) {
    val state by viewModel.uiState.collectAsState()
    val showNextWeek by viewModel.showNextWeek.collectAsState()

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
                    showNextWeek = showNextWeek,
                    onDateSelected = { viewModel.selectDate(it) },
                    onToggleNextWeek = { viewModel.toggleNextWeek() },
                    onSendTestNotification = { viewModel.sendTestNotification() }
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

@Composable
private fun WeekContent(
    state: WeekUiState.Success,
    showNextWeek: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onToggleNextWeek: () -> Unit,
    onSendTestNotification: () -> Unit
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
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val activeColor = accentColor
            val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

            TextButton(onClick = {
                if (showNextWeek) onToggleNextWeek()
            }) {
                Text(
                    text = "1 Week",
                    fontWeight = if (!showNextWeek) FontWeight.Bold else FontWeight.Normal,
                    color = if (!showNextWeek) activeColor else inactiveColor
                )
            }

            TextButton(onClick = {
                if (!showNextWeek) onToggleNextWeek()
            }) {
                Text(
                    text = "2 Weeks",
                    fontWeight = if (showNextWeek) FontWeight.Bold else FontWeight.Normal,
                    color = if (showNextWeek) activeColor else inactiveColor
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        WeekInfoCard(state = state, accentColor = accentColor)

        Spacer(modifier = Modifier.height(16.dp))

        WeekCalendar(weekStart = state.weekStart, isEvenWeek = state.isEvenWeek)

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

        if (showNextWeek) {
            Spacer(modifier = Modifier.height(32.dp))

            NextWeekCard(state = state)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onSendTestNotification,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Test Notification", color = accentColor)
        }
    }

    if (showDatePicker) {
        SimpleDatePickerDialog(
            initialDate = selectedDate,
            accentColor = accentColor,
            onDateSelected = { onDateSelected(it) },
            onDismissRequest = { showDatePicker = false }
        )
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
                text = "Next Week: ${formatDate(state.nextWeekStart)}",
                style = MaterialTheme.typography.bodyLarge,
                color = nextAccent
            )

            Text(
                text = "Week ${state.nextWeekNumber}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = nextAccent
            )

            StatusChip(isEven = state.nextWeekIsEven)

            Spacer(modifier = Modifier.height(8.dp))

            WeekCalendar(weekStart = state.nextWeekStart, isEvenWeek = state.nextWeekIsEven)
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
private val green = Color(0xFF2E7D32)
private val red = Color(0xFFC62828)

private fun dayColor(index: Int, isEvenWeek: Boolean): Color {
    val isGreen = if (isEvenWeek) {
        index == 0 || index == 3 || index == 4
    } else {
        index == 3 || index == 4
    }
    val isRed = if (isEvenWeek) {
        index == 1 || index == 2
    } else {
        index == 0 || index == 1 || index == 2
    }
    return when {
        isGreen -> green
        isRed -> red
        else -> neutralColor
    }
}

@Composable
private fun WeekCalendar(weekStart: LocalDate, isEvenWeek: Boolean) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0..6) {
            val day = weekStart.plus(i.toLong(), DateTimeUnit.DAY)
            val isToday = day == today
            val color = dayColor(i, isEvenWeek)
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
                    color = color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = day.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                    color = color
                )
            }
        }
    }
}

private fun daysInMonth(year: Int, month: Int): Int {
    val firstOfNext = if (month == 12) LocalDate(year + 1, 1, 1) else LocalDate(year, month + 1, 1)
    return firstOfNext.minus(1, DateTimeUnit.DAY).dayOfMonth
}

@Composable
private fun SimpleDatePickerDialog(
    initialDate: LocalDate,
    accentColor: Color,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentYear by remember { mutableStateOf(initialDate.year) }
    var currentMonth by remember { mutableStateOf(initialDate.monthNumber) }

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (currentMonth == 1) { currentMonth = 12; currentYear-- }
                    else currentMonth--
                }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous month")
                }
                Text(
                    text = "${monthName(currentMonth)} $currentYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    if (currentMonth == 12) { currentMonth = 1; currentYear++ }
                    else currentMonth++
                }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next month")
                }
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (label in dayLabels) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val totalDays = daysInMonth(currentYear, currentMonth)
                val firstDay = LocalDate(currentYear, currentMonth, 1)
                val startOffset = firstDay.dayOfWeek.ordinal
                val totalCells = startOffset + totalDays
                val rows = (totalCells + 6) / 7

                for (row in 0 until rows) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        for (col in 0..6) {
                            val cellIndex = row * 7 + col
                            val dayNumber = cellIndex - startOffset + 1
                            val isInMonth = dayNumber in 1..totalDays
                            val dayDate = if (isInMonth) LocalDate(currentYear, currentMonth, dayNumber) else null
                            val isSelected = dayDate == selectedDate
                            val isToday = dayDate == today

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .then(
                                        if (isSelected) Modifier
                                            .clip(CircleShape)
                                            .background(accentColor)
                                        else if (isToday) Modifier
                                            .clip(CircleShape)
                                            .border(1.5.dp, accentColor, CircleShape)
                                        else Modifier
                                    )
                                    .then(
                                        if (isInMonth && dayDate != null)
                                            Modifier.clickable {
                                                selectedDate = dayDate
                                                onDateSelected(dayDate)
                                                onDismissRequest()
                                            }
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isInMonth && dayDate != null) {
                                    Text(
                                        text = dayNumber.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) Color.White else neutralColor,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(selectedDate)
                onDismissRequest()
            }) {
                Text("OK", color = accentColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel", color = accentColor)
            }
        }
    )
}

private fun monthName(month: Int): String = when (month) {
    1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
    5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
    9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
    else -> ""
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
