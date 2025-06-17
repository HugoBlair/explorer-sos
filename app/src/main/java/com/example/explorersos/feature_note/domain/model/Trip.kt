package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class Trip(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val startLocation: String,
    val endLocation: String = startLocation,
    val startDate: String,
    val expectedEndDate: String,
    val startTime: String,
    val expectedEndTime: String,
    val isActive: Boolean = false,
    val description: String = "I am ${if (isActive) "currently on" else "planning"}" +
            " the \"$title\" trip from $startLocation to $endLocation . I am planning to leave on $startDate" +
            " and return on $expectedEndDate at $expectedEndTime.",
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
        if (startDate.isBlank()) {
            throw InvalidTripException("Start date cannot be blank.")
        }
        if (expectedEndDate.isBlank()) {
            throw InvalidTripException("End date cannot be blank.")


        }
        if (startDate > expectedEndDate) {
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

