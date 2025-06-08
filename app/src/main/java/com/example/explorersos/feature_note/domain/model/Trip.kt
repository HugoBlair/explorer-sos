package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean = false,
    val description: String = "I am ${if (isActive) "currently on" else "planning"}" +
            " the \"$title\" trip to $destination. I am planning to leave on $startDate" +
            " and return on $endDate.",
    val createdAt: Long = System.currentTimeMillis(),
    val alertId: Int? = null // FK to Alert table

) {
    init {
        if (title.isBlank()) {
            throw InvalidTripException("Title cannot be blank.")
        }
        if (destination.isBlank()) {
            throw InvalidTripException("Destination cannot be blank.")
        }
        if (startDate.isBlank()) {
            throw InvalidTripException("Start date cannot be blank.")
        }
        if (endDate.isBlank()) {
            throw InvalidTripException("End date cannot be blank.")


        }
        if (startDate > endDate) {
            throw InvalidTripException("Start date cannot be after end date.")
        }
        if (createdAt > System.currentTimeMillis()) {
            throw InvalidTripException("Created at cannot be in the future.")
        }
        if (alertId != null && alertId < 0) {
            throw InvalidTripException("Alert ID cannot be negative.")
        }
    }

    class InvalidTripException(message: String) : Exception(message)
}

