package com.weekchecker.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.weekchecker.presentation.screen.WeekCheckerScreen
import com.weekchecker.presentation.theme.WeekCheckerTheme
import com.weekchecker.presentation.screen.WeekViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun MainViewController() = ComposeUIViewController(
    configure = {}
) {
    val viewModel: WeekViewModel by inject()
    WeekCheckerTheme {
        WeekCheckerScreen(viewModel = viewModel)
    }
}
