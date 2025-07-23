package com.example.explorersos.feature_trip.data.repository

import com.example.explorersos.feature_trip.data.data_source.AlertRecipientDao
import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.repository.AlertRecipientRepository
import kotlinx.coroutines.flow.Flow

class AlertRecipientRepositoryImpl(private val alertRecipientDao: AlertRecipientDao) :
    AlertRecipientRepository {
    override fun getAlertRecipients(): Flow<List<AlertRecipient>> {
        return alertRecipientDao.getAlertRecipients()
    }

    override suspend fun getAlertRecipientById(id: Int): AlertRecipient? {
        return alertRecipientDao.getAlertRecipientById(id)
    }

    override suspend fun insertAlertRecipient(alertRecipient: AlertRecipient) {
        return alertRecipientDao.insertAlertRecipient(alertRecipient)
    }

    override suspend fun deleteAlertRecipient(alertRecipient: AlertRecipient) {
        return alertRecipientDao.deleteAlertRecipient(alertRecipient)
    }


}