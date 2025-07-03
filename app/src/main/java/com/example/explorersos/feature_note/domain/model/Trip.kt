package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.explorersos.feature_note.domain.util.DateTimeUtils.getFormattedDisplayTime
import java.time.Instant
import java.time.ZonedDateTime

@Entity
data class Trip(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val startLocation: String,
    val endLocation: String = startLocation,
    val startDateTime: Instant,
    val expectedEndDateTime: Instant,
    val isActive: Boolean = false,
    val description: String = "I am ${if (isActive) "currently on" else "planning"}" +
            " the \"$title\" trip from $startLocation to $endLocation . I am planning to leave on ${
                getFormattedDisplayTime(
                    startDateTime
                )
            }" +
            " and return on ${getFormattedDisplayTime(expectedEndDateTime)}.",
    val createdAt: String = ZonedDateTime.now().toString(),
    val alertId: Int? = null // FK to Alert table

) {
    init {
        if (title.isBlank()) {
            throw InvalidTripException("Title cannot be blank.")
        }
        if (startLocation.isBlank()) {
            throw InvalidTripException("Destination cannot be blank.")
        }
        if (startDateTime.isAfter(expectedEndDateTime)) {
            throw InvalidTripException("Start date cannot be after end date.")
        }
        if (createdAt > ZonedDateTime.now().toString()) {
            throw InvalidTripException("Created at cannot be in the future.")
        }
        if (alertId != null && alertId < 0) {
            throw InvalidTripException("Alert ID cannot be negative.")
        }
    }

    class InvalidTripException(message: String) : Exception(message)
}

