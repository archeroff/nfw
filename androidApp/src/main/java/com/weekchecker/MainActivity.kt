package com.weekchecker

import android.app.ActivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.weekchecker.presentation.screen.WeekCheckerScreen
import com.weekchecker.presentation.theme.WeekCheckerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.weekchecker.presentation.screen.WeekViewModel

class MainActivity : ComponentActivity() {
    private val weekViewModel: WeekViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            setTaskDescription(ActivityManager.TaskDescription(null, R.mipmap.ic_launcher))
        }
        setContent {
            WeekCheckerTheme {
                WeekCheckerScreen(viewModel = weekViewModel)
            }
        }
    }
}
