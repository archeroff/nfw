package com.weekchecker.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B5E20),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF002204),
    secondary = Color(0xFF4E6352),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D2),
    onSecondaryContainer = Color(0xFF0C1F12),
    tertiary = Color(0xFF3B6470),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBFEAF7),
    onTertiaryContainer = Color(0xFF001F28),
    background = Color(0xFFF8FBF4),
    onBackground = Color(0xFF1A1C19),
    surface = Color(0xFFF8FBF4),
    onSurface = Color(0xFF1A1C19),
    surfaceVariant = Color(0xFFDDE5D9),
    onSurfaceVariant = Color(0xFF424940),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8ADB8F),
    onPrimary = Color(0xFF00390A),
    primaryContainer = Color(0xFF005314),
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = Color(0xFFB5CCBA),
    onSecondary = Color(0xFF213527),
    secondaryContainer = Color(0xFF374B3B),
    onSecondaryContainer = Color(0xFFD0E8D2),
    tertiary = Color(0xFFA3CEDB),
    onTertiary = Color(0xFF03363F),
    tertiaryContainer = Color(0xFF214C57),
    onTertiaryContainer = Color(0xFFBFEAF7),
    background = Color(0xFF1A1C19),
    onBackground = Color(0xFFE2E3DC),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DC),
    surfaceVariant = Color(0xFF424940),
    onSurfaceVariant = Color(0xFFC2C9BD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): androidx.compose.material3.ColorScheme?

@Composable
fun WeekCheckerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = dynamicColorScheme(darkTheme)
        ?: if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
