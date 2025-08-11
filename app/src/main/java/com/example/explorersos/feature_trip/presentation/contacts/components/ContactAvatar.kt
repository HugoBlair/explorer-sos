package com.example.explorersos.feature_trip.presentation.contacts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize

@Composable
fun ContactAvatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier
) {
    // Generate initials from the name
    val initials = buildString {
        if (firstName.isNotBlank()) append(firstName.first().uppercaseChar())
        if (lastName.isNotBlank()) append(lastName.first().uppercaseChar())
    }

    // Combine names for consistent hashing
    val fullName = "$firstName $lastName"

    // Generate a stable color from the name's hash code
    val backgroundColor = remember(fullName) {
        val hash = fullName.hashCode()
        Color(
            red = (hash and 0xFF0000 shr 16) / 255f,
            green = (hash and 0x00FF00 shr 8) / 255f,
            blue = (hash and 0x0000FF) / 255f
        ).copy(alpha = 0.5f) // Use alpha to get softer, pastel-like colors
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    // Calculate the font size based on the measured width of the Box.
    // The font size will update whenever the measured size changes.
    val fontSize = with(LocalDensity.current) {
        // A divisor of 2.5 provides a good visual balance for 1-2 initials.
        (size.width / 2.5f).toSp()
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                // When the Box's size is determined, update our state.
                size = it
            }
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            // The calculated, responsive font size is applied here.
            style = MaterialTheme.typography.titleMedium.copy(fontSize = fontSize)
        )
    }
}