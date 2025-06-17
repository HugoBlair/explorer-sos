package com.example.explorersos.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.explorersos.feature_note.presentation.navigation.AppNavigation
import com.example.explorersos.ui.theme.ExplorerSOSTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ExplorerSOSTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )


            }
        }
    }
}
