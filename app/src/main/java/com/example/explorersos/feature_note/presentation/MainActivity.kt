package com.example.explorersos.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.presentation.trips.components.TripItem
import com.example.explorersos.ui.theme.ExplorerSOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExplorerSOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TripItem(
                        Trip(
                            id = 1,
                            title = "Big Climb",
                            destination = "Mt Kaukau",
                            startDate = "2025-01-01T00:00:00Z",
                            endDate = "2025-01-02T00:00:00Z",
                            createdAt = "2024-12-31T23:59:59Z"
                        ),
                        modifier = Modifier.padding(innerPadding),
                        onClick = {}


                    )
                    TripItem(
                        Trip(
                            id = 1,
                            title = "Big Climb",
                            destination = "Mt Kaukau",
                            startDate = "2025-01-01T00:00:00Z",
                            endDate = "2025-01-02T00:00:00Z",
                            createdAt = "2024-12-31T23:59:59Z"
                        ),
                        modifier = Modifier.padding(innerPadding),
                        onClick = {}


                    )

                }
            }
        }
    }
}
