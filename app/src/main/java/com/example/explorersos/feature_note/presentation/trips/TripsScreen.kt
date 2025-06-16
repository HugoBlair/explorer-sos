package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_note.presentation.navigation.AddEditNoteScreenRoute
import com.example.explorersos.feature_note.presentation.trips.TripsEvent
import com.example.explorersos.feature_note.presentation.trips.TripsViewModel
import com.example.explorersos.feature_note.presentation.trips.components.OrderSection
import com.example.explorersos.feature_note.presentation.trips.components.TripItem

// Assuming you have defined your type-safe routes like this:
// @Serializable
// object NotesScreenRoute
//
// @Serializable
// data class AddEditNoteScreenRoute(val noteId: Int? = null, val noteColor: Int? = null)

@OptIn(ExperimentalMaterial3Api::class) // Opt-in for Scaffold API
@ExperimentalAnimationApi
@Composable
fun TripScreen(
    navController: NavController,
    viewModel: TripsViewModel
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddEditTripScreenRoute())

                },
                // M3: backgroundColor is now containerColor
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Trip")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding -> // M3: Scaffold provides content padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                // M3: Apply the padding provided by the Scaffold
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your notes",
                    // M3: The typography scale has changed. h4 is now headlineMedium
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    onClick = {
                        viewModel.onEvent(TripsEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    tripOrder = state.tripOrder,
                    onOrderChange = {
                        viewModel.onEvent(TripsEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.trips) { trip ->
                    TripItem(
                        trip = trip,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    AddEditNoteScreenRoute(tripId = trip.id)
                                )

                            }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}