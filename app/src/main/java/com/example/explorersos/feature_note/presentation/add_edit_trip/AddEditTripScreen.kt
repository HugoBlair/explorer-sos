package com.example.explorersos.feature_note.presentation.add_edit_trip

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
import com.example.explorersos.feature_note.domain.util.DateTimeUtils.getFormattedDisplayTime
import com.example.explorersos.feature_note.presentation.add_edit_trip.components.TransparentHintTextField
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
    val isActive = viewModel.isActive.value
    val isRoundTrip = viewModel.isRoundTrip.value


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
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditTripEvent.SaveTrip)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Trip")
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

            // Trip Status Toggle
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
                        text = "Trip Status",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isActive) "Active" else "Inactive",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Switch(
                            checked = isActive,
                            onCheckedChange = {
                                viewModel.onEvent(AddEditTripEvent.ToggleActiveStatus)
                            }
                        )
                    }
                }
            }

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
            TransparentHintTextField(
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
                textStyle = MaterialTheme.typography.bodyLarge
            )

            // End Location (only show if not round trip)
            if (!isRoundTrip) {
                TransparentHintTextField(
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
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }

            // Date and Time Section
            Text(
                text = "Pick your trip's starting date and time",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )




            DateTimePicker(
                initialTimestamp = startDateTimeState.selectedDateTime?.toEpochMilli()
                    ?: Instant.now().toEpochMilli(),
                onTimestampSelected = { newTimestamp ->
                    viewModel.onEvent(
                        AddEditTripEvent.EnteredStartDateTime(
                            Instant.ofEpochMilli(
                                newTimestamp
                            )
                        )
                    )
                }
            ) { launchPicker ->
                Button(onClick = launchPicker) {
                    Text("Select Start Date & Time")
                }
            }
            Text(
                text = getFormattedDisplayTime(startDateTimeState.selectedDateTime),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
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
                    Text("Select A End Date & Time")
                }
            }
            Text(
                text = getFormattedDisplayTime(endDateTimeState.selectedDateTime),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
