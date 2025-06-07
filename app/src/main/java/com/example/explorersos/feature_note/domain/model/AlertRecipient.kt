package com.example.explorersos.feature_note.domain.model

import android.util.Patterns
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlertRecipient(
    @PrimaryKey val id: Int? = null,
    val recipientEmail: String?,
    val recipientPhone: String?
    ){
    init {
        if (recipientEmail.isNullOrBlank() && recipientPhone.isNullOrBlank()) {
            throw InvalidAlertRecipientException("Either email or phone must be provided.")
        }
        if (recipientEmail != null && !Patterns.EMAIL_ADDRESS.matcher(recipientEmail).matches()) {
            throw InvalidAlertRecipientException("Invalid email format.")
        }
        if(recipientPhone!= null && !Patterns.PHONE.matcher(recipientPhone).matches()) {
            throw InvalidAlertRecipientException("Invalid phone number format.")
        }
    }
}

class InvalidAlertRecipientException(message: String): Exception(message)

