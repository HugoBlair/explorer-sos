package com.example.explorersos.feature_trip.presentation.trips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_trip.presentation.navigation.Routes.AddEditTripScreenRoute
import com.example.explorersos.feature_trip.presentation.trips.components.OrderSection
import com.example.explorersos.feature_trip.presentation.trips.components.TripItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripsViewModel
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    // This effect handles showing snackbars from undoable actions (delete, end)
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TripsViewModel.UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Long
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        when (event.action) {
                            "Undo" -> {
                                if (event.message == "Trip deleted") {
                                    viewModel.onEvent(TripsEvent.RestoreTrip)
                                } else if (event.message == "Trip ended") {
                                    viewModel.onEvent(TripsEvent.RestoreEndedTrip)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // This effect handles showing snackbars from one-off events (like saving a trip)
    // by observing the result from the previous screen.
    LaunchedEffect(key1 = true) {
        val message = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("snackbar_message")
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            // Clear the message so it doesn't appear again
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("snackbar_message")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddEditTripScreenRoute())

                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Trip")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Trips",
                    style = MaterialTheme.typography.headlineMedium
                )
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Layers"
                )
            }

            val (activeTrips, inactiveTrips) = state.trips.partition { it.isActive }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Ongoing Trip Section
                if (activeTrips.isNotEmpty()) {
                    item {
                        Text(
                            text = "Ongoing Trip",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(activeTrips) { trip ->
                        TripItem(
                            trip = trip,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onEndTripClick = { viewModel.onEvent(TripsEvent.EndTrip(trip)) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // All Trips Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "All trips",
                            style = MaterialTheme.typography.titleMedium
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
                }

                // Order Section
                item {
                    AnimatedVisibility(
                        visible = state.isOrderSectionVisible,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        OrderSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 16.dp),
                            tripOrder = state.tripOrder,
                            onOrderChange = {
                                viewModel.onEvent(TripsEvent.Order(it))
                            }
                        )
                    }
                }

                // Inactive Trips List
                items(inactiveTrips) { trip ->
                    TripItem(
                        trip = trip,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onEditClick = {
                            navController.navigate(
                                AddEditTripScreenRoute(tripId = trip.id ?: -1)
                            )
                        },
                        //onDeleteClick = {
                        //    viewModel.onEvent(TripsEvent.DeleteTrip(trip))
                        //}
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}