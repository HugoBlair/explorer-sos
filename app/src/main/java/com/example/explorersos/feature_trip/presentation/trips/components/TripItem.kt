package com.example.explorersos.feature_trip.presentation.trips.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.util.DateTimeUtils

@Composable
fun TripItem(
    trip: Trip,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onEndTripClick: () -> Unit = {}
) {
    if (trip.isActive) {
        // Larger, more detailed layout for active trips
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                ) {
                    TopographicPattern(modifier = Modifier.fillMaxSize())
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = trip.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = trip.startLocation,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Start", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = DateTimeUtils.getFormattedDisplayTime(trip.startDateTime),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("End", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = DateTimeUtils.getFormattedDisplayTime(trip.expectedEndDateTime),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onEndTripClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("End Trip")
                    }
                }
            }
        }
    } else {
        // Smaller layout for inactive trips
        Card(
            modifier = modifier.clickable { onEditClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = DateTimeUtils.getRelativeTimeString(trip.createdAt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color(0xFFE8F5E8),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    TopographicPattern(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun TopographicPattern(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val paint = Paint().asFrameworkPaint().apply {
            color = android.graphics.Color.parseColor("#90C695")
            strokeWidth = 2.dp.toPx()
            style = android.graphics.Paint.Style.STROKE
        }

        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw concentric irregular circles to simulate topographic lines
        val centerX = canvasWidth * 0.6f
        val centerY = canvasHeight * 0.4f

        drawIntoCanvas { canvas ->
            // Create multiple topographic rings
            for (i in 1..8) {
                val baseRadius = i * 6.dp.toPx()
                val path = android.graphics.Path()

                var firstPoint = true
                for (angle in 0..360 step 10) {
                    val radians = Math.toRadians(angle.toDouble())
                    // Add some irregularity to make it look more natural
                    val radiusVariation = baseRadius + kotlin.math.sin(radians * 3) * 2.dp.toPx()
                    val x = centerX + kotlin.math.cos(radians) * radiusVariation
                    val y = centerY + kotlin.math.sin(radians) * radiusVariation

                    if (firstPoint) {
                        path.moveTo(x.toFloat(), y.toFloat())
                        firstPoint = false
                    } else {
                        path.lineTo(x.toFloat(), y.toFloat())
                    }
                }
                path.close()
                canvas.nativeCanvas.drawPath(path, paint)
            }

            // Add a few more irregular shapes for variety
            val secondCenterX = canvasWidth * 0.3f
            val secondCenterY = canvasHeight * 0.7f

            for (i in 1..4) {
                val baseRadius = i * 4.dp.toPx()
                val path = android.graphics.Path()

                var firstPoint = true
                for (angle in 0..360 step 15) {
                    val radians = Math.toRadians(angle.toDouble())
                    val radiusVariation = baseRadius + kotlin.math.sin(radians * 4) * 1.5.dp.toPx()
                    val x = secondCenterX + kotlin.math.cos(radians) * radiusVariation
                    val y = secondCenterY + kotlin.math.sin(radians) * radiusVariation

                    if (firstPoint) {
                        path.moveTo(x.toFloat(), y.toFloat())
                        firstPoint = false
                    } else {
                        path.lineTo(x.toFloat(), y.toFloat())
                    }
                }
                path.close()
                canvas.nativeCanvas.drawPath(path, paint)
            }
        }
    }

}