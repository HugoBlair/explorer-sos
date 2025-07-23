package com.example.explorersos.feature_trip.domain.repository

import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import kotlinx.coroutines.flow.Flow

interface AlertRecipientRepository {

    fun getAlertRecipients(): Flow<List<AlertRecipient>>

    suspend fun getAlertRecipientById(id: Int): AlertRecipient?

    suspend fun insertAlertRecipient(alertRecipient: AlertRecipient)

    suspend fun deleteAlertRecipient(alertRecipient: AlertRecipient)

}