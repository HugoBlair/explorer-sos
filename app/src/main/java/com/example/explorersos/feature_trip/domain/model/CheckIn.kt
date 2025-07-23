package com.example.explorersos.feature_trip.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.explorersos.feature_trip.domain.util.CheckInStatus
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE // If a trip is deleted, its check-ins are also deleted.
        )
    ]
)
data class CheckIn(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val tripId: Int, // The foreign key linking it to a Trip
    val dueDateTime: Instant,
    val status: CheckInStatus = CheckInStatus.PENDING,
    val description: String? = null // Optional: e.g., "Reached Summit", "Camp for Night 1"
)