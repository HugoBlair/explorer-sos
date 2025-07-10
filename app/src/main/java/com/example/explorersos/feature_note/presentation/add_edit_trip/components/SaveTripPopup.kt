package com.example.explorersos.feature_note.presentation.add_edit_trip.components

import DateTimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_note.domain.util.DateTimeUtils.getFormattedDisplayTime
import java.time.Instant

@Composable
fun SaveTripPopup(
    onStartNow: () -> Unit,
    onStartLater: (Instant) -> Unit,
    onCancel: () -> Unit,
    content: @Composable (launchPopup: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf<Instant?>(null) }

    content {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onCancel()
            },
            title = {
                Text(
                    text = "Start Your Trip",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Would you like to start your trip right now?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (showDatePicker) {
                        Text(
                            text = "Select your trip's start date and time:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        DateTimePicker(
                            initialTimestamp = selectedDateTime?.toEpochMilli()
                                ?: Instant.now().toEpochMilli(),
                            onTimestampSelected = { newTimestamp ->
                                selectedDateTime = Instant.ofEpochMilli(newTimestamp)
                            }
                        ) { launchPicker ->
                            OutlinedButton(
                                onClick = launchPicker,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val dateTime = getFormattedDisplayTime(selectedDateTime)
                                    Text(
                                        text = if (dateTime == "Non Valid Date") {
                                            "Select Start Date & Time"
                                        } else {
                                            dateTime
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Edit Start Date/Time"
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (showDatePicker) {
                    Button(
                        onClick = {
                            selectedDateTime?.let { dateTime ->
                                onStartLater(dateTime)
                                showDialog = false
                                showDatePicker = false
                            }
                        },
                        enabled = selectedDateTime != null
                    ) {
                        Text("Confirm Date")
                    }
                } else {
                    Row {
                        Button(
                            onClick = {
                                onStartNow()
                                showDialog = false
                            }
                        ) {
                            Text("Start Now")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = {
                                showDatePicker = true
                            }
                        ) {
                            Text("Start Later")
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (showDatePicker) {
                            showDatePicker = false
                        } else {
                            showDialog = false
                            onCancel()
                        }
                    }
                ) {
                    Text(if (showDatePicker) "Back" else "Cancel")
                }
            }
        )
    }
}