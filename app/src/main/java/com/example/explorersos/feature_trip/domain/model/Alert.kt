package com.example.explorersos.feature_trip.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.explorersos.feature_trip.domain.util.ContactMethod

@Entity
data class Alert(
    @PrimaryKey val id: Int? = null,
    val alertMessage: String,
    val alertHoursAfter: Int = 24,
    val alertInstructions: String,
    val alertOrder: List<ContactMethod>,
    val recipientsId: List<Int>,
    /**
     * If true, this alert will also be triggered if any check-in is missed.
     */
    val triggerOnMissedCheckIn: Boolean = false,
    val checkInMissedMessage: String? = null

) {
    init {
        if (alertMessage.isBlank()) {
            throw InvalidAlertException("Alert message cannot be blank.")
        }
        if (alertInstructions.isBlank()) {
            throw InvalidAlertException("Alert instructions cannot be blank.")
        }
        if (alertOrder.isEmpty()) {
            throw InvalidAlertException("At least one contact method must be provided.")
        }
        if (recipientsId.isEmpty()) {
            throw InvalidAlertException("At least one recipient ID must be provided.")
        }
        if (alertHoursAfter <= 0) {
            throw InvalidAlertException("Alert time must be greater than zero.")
        }
    }

    class InvalidAlertException(message: String) : Exception(message)
}