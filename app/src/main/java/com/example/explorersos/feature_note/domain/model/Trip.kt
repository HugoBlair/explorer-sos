package com.example.explorersos.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip (
    @PrimaryKey val id: Int? = null,
    val title: String,
    val destination: String,
    val startDate:String,
    val endDate:String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false,
    val alertId: Alert? = null // FK to Alert table

)
class InvalidTripException(message: String): Exception(message)

