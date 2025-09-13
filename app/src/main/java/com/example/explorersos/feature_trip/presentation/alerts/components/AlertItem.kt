package com.example.explorersos.feature_trip.presentation.alerts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.SpeakerNotes
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.model.Contact

/**
 * A card that displays a summary of an alert and can be expanded to show full details.
 *
 * @param alert The alert data to display.
 * @param recipients A list of the resolved Contact objects for this alert. This should be
 * prepared by the ViewModel to keep the Composable clean.
 * @param modifier Modifier for this composable.
 * @param onEditClick Callback for when the "Edit" button is clicked.
 * @param onDeleteClick Callback for when the "Delete" button is clicked.
 */
@Composable
fun AlertItem(
    alert: Alert,
    recipients: List<Contact>, // Pass in the resolved contacts
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // State to track if the card is expanded or not
    var isExpanded by remember { mutableStateOf(false) }

    // Animation for the chevron icon rotation
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 90f else 0f)

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded } // Toggle expansion on click
                .padding(16.dp)
        ) {
            // --- Collapsed (Summary) View ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = "Alert Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = alert.alertMessage,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Triggers after ${alert.alertHoursAfter} hours",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }

            // --- Expanded (Detailed) View ---
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    // Recipients Section
                    DetailSection(
                        icon = Icons.Default.People,
                        title = "Recipients"
                    ) {
                        recipients.forEach { contact ->
                            Text("- ${contact.firstName} ${contact.lastName}")
                        }
                        if (recipients.isEmpty()) {
                            Text("No recipients selected.", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instructions Section
                    DetailSection(
                        icon = Icons.Default.SpeakerNotes,
                        title = "Instructions for Contacts"
                    ) {
                        Text(alert.alertInstructions)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Delivery Section
                    DetailSection(
                        icon = Icons.Default.Sms,
                        title = "Delivery Methods"
                    ) {
                        Text(alert.alertOrder.joinToString(", "))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDeleteClick) {
                            Text("Delete")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onEditClick) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}

/**
 * A helper composable to create a consistent layout for detail sections.
 */
@Composable
private fun DetailSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}