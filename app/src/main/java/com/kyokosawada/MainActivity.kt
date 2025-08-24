package com.kyokosawada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kyokosawada.ui.utils.calculateWindowSizeClass
import com.kyokosawada.ui.navigation.AppNavHost
import com.kyokosawada.ui.theme.AppTheme
import com.kyokosawada.data.settings.SettingsRepository
import org.koin.android.ext.android.inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository by inject()
            val darkMode by settingsRepository.settingsFlow.collectAsState()
            val windowSizeClass = calculateWindowSizeClass()
            AppTheme(darkTheme = darkMode.darkMode, dynamicColor = false) {
                AppNavHost(windowSizeClass)
            }
        }
    }
}

