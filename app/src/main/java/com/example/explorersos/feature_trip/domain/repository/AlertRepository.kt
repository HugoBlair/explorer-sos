package com.example.explorersos.feature_trip.domain.repository

import com.example.explorersos.feature_trip.domain.model.Alert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {

    fun getAlerts(): Flow<List<Alert>>

    suspend fun getAlertById(id: Int): Alert?

    suspend fun insertAlert(alert: Alert)

    suspend fun deleteAlert(alert: Alert)

}