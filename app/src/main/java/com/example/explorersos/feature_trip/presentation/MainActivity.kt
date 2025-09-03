package com.example.explorersos.feature_trip.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.explorersos.ui.theme.ExplorerSOSTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                ExplorerSOSTheme {
                    MainScreen()
                }
            }
        }
    }
}