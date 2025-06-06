package com.example.explorersos.feature_note.domain.model

data class Trip (
    val id: Int? = null,
    val title: String,
    val destination: String,
    val startDate:String,
    val endDate:String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)

