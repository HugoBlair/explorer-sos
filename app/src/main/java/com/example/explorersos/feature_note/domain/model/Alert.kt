package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.explorersos.feature_note.domain.util.ContactMethod

@Entity
data class Alert(
    @PrimaryKey val id: Int? = null,
    val alertHoursAfter: Int = 24,
    val alertInstructions: String,
    val alertOrder: List<ContactMethod>,
    val recipients: List<AlertRecipient>,
)

class invalidAlertException(message: String): Exception(message)