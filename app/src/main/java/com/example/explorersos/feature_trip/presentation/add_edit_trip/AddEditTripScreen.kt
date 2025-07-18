package com.example.explorersos.feature_trip.presentation.add_edit_trip

import DateTimePicker
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

    val startLocationPredictions = viewModel.startLocationPredictions.value
    val endLocationPredictions = viewModel.endLocationPredictions.value


    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditTripViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }

                is AddEditTripViewModel.UiEvent.SaveTrip -> {
                    navController.navigateUp()
                }
            }
        }
    }


    Scaffold(
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
            //TODO()
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)


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
                modifier = Modifier.height(100.dp)
            )

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
            }
            Spacer(modifier = Modifier.height(16.dp))

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
        }
    }
    Spacer(modifier = Modifier.height(72.dp))
}