package com.example.explorersos.feature_trip.presentation.add_edit_trip

import DateTimePicker
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_trip.domain.util.DateTimeUtils.getFormattedDisplayTime
import com.example.explorersos.feature_trip.presentation.add_edit_trip.components.PlacesAutocompleteTextField
import com.example.explorersos.feature_trip.presentation.add_edit_trip.components.SaveTripPopup
import com.example.explorersos.feature_trip.presentation.add_edit_trip.components.TransparentHintTextField
import kotlinx.coroutines.launch
import java.time.Instant

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTripScreen(
    navController: NavController,
    viewModel: AddEditTripViewModel,
    tripId: Int = -1
) {
    val titleState = viewModel.tripTitle.value
    val descriptionState = viewModel.tripDescription.value
    val startLocationState = viewModel.tripStartLocation.value
    val endLocationState = viewModel.tripEndLocation.value
    val startDateTimeState = viewModel.tripStartDateTime.value
    val endDateTimeState = viewModel.tripEndDateTime.value
    val isRoundTrip = viewModel.isRoundTrip.value
    val hasUnsavedChanges by viewModel.hasUnsavedChanges

    val startLocationPredictions = viewModel.startLocationPredictions.value
    val endLocationPredictions = viewModel.endLocationPredictions.value


    val snackbarHostState = remember { SnackbarHostState() }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // --- Back Navigation Handling ---
    val onBackPress: () -> Unit = {
        if (hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            navController.navigateUp()
        }
    }

    BackHandler(enabled = true, onBack = onBackPress)

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard Changes?") },
            text = { Text("If you go back, your changes will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog = false
                        navController.navigateUp() // Proceed with navigation
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    // --- End Back Navigation Handling ---

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditTripViewModel.UiEvent.ShowErrorSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }

                is AddEditTripViewModel.UiEvent.SaveTripSuccess -> {
                    // Pass the result message back to the previous screen
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", event.message)
                    navController.navigateUp()
                }

                is AddEditTripViewModel.UiEvent.DeleteTripSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", event.message)
                    navController.navigateUp()
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Trip?") },
            text = { Text("Are you sure you want to permanently delete this trip?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(AddEditTripEvent.DeleteTrip)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (tripId == -1) "New Trip" else "Edit Trip") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) { // Use our custom back handler
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (tripId != -1) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Trip"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            SaveTripPopup(
                existingStartDate = startDateTimeState.selectedDateTime,
                onStartNow = {
                    // Set start date to now and active status, then save
                    viewModel.onEvent(AddEditTripEvent.EnteredStartDateTime(Instant.now()))
                    viewModel.onEvent(AddEditTripEvent.SetActiveStatus(true))
                    viewModel.onEvent(AddEditTripEvent.SaveTrip)
                },
                onStartLaterWithNewDate = { selectedDateTime ->
                    // Set selected start date and inactive status, then save
                    viewModel.onEvent(AddEditTripEvent.SetActiveStatus(false))
                    viewModel.onEvent(AddEditTripEvent.EnteredStartDateTime(selectedDateTime))
                    viewModel.onEvent(AddEditTripEvent.SaveTrip)
                },
                onStartLaterWithExistingDate = {
                    // Set inactive status and save with the existing date
                    viewModel.onEvent(AddEditTripEvent.SetActiveStatus(false))
                    viewModel.onEvent(AddEditTripEvent.SaveTrip)
                },
                onCancel = {
                    // Do nothing, just close the popup
                }
            ) { launchPopup ->
                FloatingActionButton(
                    onClick = {
                        // Always show the popup on click
                        launchPopup()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save Trip")
                }

            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                // Add vertical scroll to handle smaller screens or when keyboard is open
                .verticalScroll(rememberScrollState())
        ) {
            //Title
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTripEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTripEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Trip Description
            TransparentHintTextField(
                text = descriptionState.text,
                hint = descriptionState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTripEvent.EnteredDescription(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTripEvent.ChangeDescriptionFocus(it))
                },
                isHintVisible = descriptionState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.height(100.dp),
                singleLine = false // Allow multiple lines for description
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Round Trip Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Round Trip",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isRoundTrip) "Yes" else "No",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isRoundTrip) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Switch(
                            checked = isRoundTrip,
                            onCheckedChange = {
                                viewModel.onEvent(AddEditTripEvent.ToggleRoundTrip)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Start Location
            PlacesAutocompleteTextField(
                text = startLocationState.text,
                hint = startLocationState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTripEvent.EnteredStartLocation(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTripEvent.ChangeStartLocationFocus(it))
                },
                isHintVisible = startLocationState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                predictions = startLocationPredictions,
                onPredictionSelected = { prediction ->
                    viewModel.onEvent(AddEditTripEvent.SelectStartLocation(prediction))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // End Location (only show if not round trip)
            if (!isRoundTrip) {
                PlacesAutocompleteTextField(
                    text = endLocationState.text,
                    hint = endLocationState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditTripEvent.EnteredEndLocation(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditTripEvent.ChangeEndLocationFocus(it))
                    },
                    isHintVisible = endLocationState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    predictions = endLocationPredictions,
                    onPredictionSelected = { prediction ->
                        viewModel.onEvent(AddEditTripEvent.SelectEndLocation(prediction))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            // Edit Start Date/Time (only visible when editing an existing trip)
            if (tripId != -1 && startDateTimeState.selectedDateTime != null) {
                Text(
                    text = "Edit your trip's Start date and time",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                DateTimePicker(
                    initialTimestamp = startDateTimeState.selectedDateTime!!.toEpochMilli(),
                    onTimestampSelected = { newTimestamp ->
                        viewModel.onEvent(
                            AddEditTripEvent.EnteredStartDateTime(
                                Instant.ofEpochMilli(newTimestamp)
                            )
                        )
                    }
                ) { launchPicker ->
                    Button(onClick = launchPicker) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = getFormattedDisplayTime(startDateTimeState.selectedDateTime),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                imageVector = Icons.Default.EditCalendar,
                                contentDescription = "Edit Start Date/Time"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            Text(
                text = "Pick your trip's End date and time",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            DateTimePicker(
                initialTimestamp = endDateTimeState.selectedDateTime?.toEpochMilli()
                    ?: Instant.now().toEpochMilli(),
                onTimestampSelected = { newTimestamp ->
                    viewModel.onEvent(
                        AddEditTripEvent.EnteredEndDateTime(
                            Instant.ofEpochMilli(
                                newTimestamp
                            )
                        )
                    )
                }
            ) { launchPicker ->
                Button(onClick = launchPicker) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        val dateTime = getFormattedDisplayTime(endDateTimeState.selectedDateTime)
                        if (dateTime == "Non Valid Date") {
                            Text(
                                text = "Add Your Trip's End Date & Time",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        } else {

                            Text(
                                text = dateTime,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.EditCalendar,
                            contentDescription = "Edit End Date/Time"
                        )

                    }
                }
            }
            // Add spacer at the bottom to ensure content isn't hidden by the FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}