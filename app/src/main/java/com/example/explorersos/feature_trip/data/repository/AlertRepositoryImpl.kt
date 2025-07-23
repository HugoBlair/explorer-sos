package com.example.explorersos.feature_trip.data.repository

import com.example.explorersos.feature_trip.data.data_source.AlertDao
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow


class AlertRepositoryImpl(private val alertDao: AlertDao) : AlertRepository {
    override fun getAlerts(): Flow<List<Alert>> {
        return alertDao.getAlerts()

    }

    override suspend fun getAlertById(id: Int): Alert? {
        return alertDao.getAlertById(id)
    }

    override suspend fun insertAlert(alert: Alert) {
        alertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertDao.deleteAlert(alert)
    }
}