package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlertRecipient(
    @PrimaryKey val id: Int? = null,
    val recipientEmail: String?,
    val recipientPhone: Int?
    )

class invalidAlertRecipientException(message: String): Exception(message)

