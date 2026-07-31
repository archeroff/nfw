package com.weekchecker.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.weekchecker.di.appModule
import com.weekchecker.di.webNotificationModule
import com.weekchecker.presentation.screen.WeekCheckerScreen
import com.weekchecker.presentation.screen.WeekViewModel
import com.weekchecker.presentation.theme.WeekCheckerTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule, webNotificationModule)
    }
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()
    }
}

@Composable
fun App() {
    val viewModel: WeekViewModel = koinViewModel()
    WeekCheckerTheme {
        WeekCheckerScreen(viewModel = viewModel)
    }
}
