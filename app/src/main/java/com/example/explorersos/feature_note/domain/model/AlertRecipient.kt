package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
val PHONE_REGEX = Regex("""^\+?(\d{1,3})?[-.\s]?(\(?\d{3}\)?[-.\s]?)?(\d[-.\s]?){6,9}\d$""")

@Entity
data class AlertRecipient(
    @PrimaryKey val id: Int? = null,
    val recipientEmail: String?,
    val recipientPhone: String?
) {
    init {
        if (recipientEmail.isNullOrBlank() && recipientPhone.isNullOrBlank()) {
            throw InvalidAlertRecipientException("Either email or phone must be provided.")
        }
        if (recipientEmail != null && !EMAIL_REGEX.matches(recipientEmail)) {
            throw InvalidAlertRecipientException("Invalid email format.")
        }
        if (recipientPhone != null && !PHONE_REGEX.matches(recipientPhone)) {
            throw InvalidAlertRecipientException("Invalid phone number format.")
        }
    }
}

class InvalidAlertRecipientException(message: String) : Exception(message)

