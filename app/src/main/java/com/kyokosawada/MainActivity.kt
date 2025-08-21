package com.kyokosawada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kyokosawada.ui.calculateWindowSizeClass
import com.kyokosawada.ui.navigation.AppNavHost
import com.kyokosawada.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass()
            AppTheme {
                AppNavHost(windowSizeClass)
            }
        }
    }
}

