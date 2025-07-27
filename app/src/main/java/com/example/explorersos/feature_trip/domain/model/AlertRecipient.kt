package com.example.explorersos.feature_trip.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
val PHONE_REGEX = Regex("""^\+?(\d{1,3})?[-.\s]?(\(?\d{3}\)?[-.\s]?)?(\d[-.\s]?){6,9}\d$""")

@Entity
data class Contact(
    @PrimaryKey val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val notes: String?
) {
    init {
        if (email.isNullOrBlank() && phone.isNullOrBlank()) {
            throw InvalidContactException("Either email or phone must be provided.")
        }
        if (email != null && !EMAIL_REGEX.matches(email)) {
            throw InvalidContactException("Invalid email format.")
        }
        if (phone != null && !PHONE_REGEX.matches(phone)) {
            throw InvalidContactException("Invalid phone number format.")
        }
    }

    class InvalidContactException(message: String) : Exception(message)
}



