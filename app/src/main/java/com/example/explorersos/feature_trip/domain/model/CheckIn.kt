package com.example.explorersos.feature_trip.domain.model

import androidx.room.PrimaryKey
import com.example.explorersos.feature_trip.domain.util.CheckInStatus
import java.time.Instant

data class CheckIn(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val tripId: Int, // The foreign key linking it to a Trip
    val dueDateTime: Instant,
    val status: CheckInStatus = CheckInStatus.PENDING,
    val description: String? = null // Optional: e.g., "Reached Summit", "Camp for Night 1"
)