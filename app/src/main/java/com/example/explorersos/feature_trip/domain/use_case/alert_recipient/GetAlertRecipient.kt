package com.example.explorersos.feature_trip.domain.use_case.alert_recipient

import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.repository.AlertRecipientRepository

class GetAlertRecipient(private val repository: AlertRecipientRepository) {
    suspend operator fun invoke(id: Int): AlertRecipient? {
        return repository.getAlertRecipientById(id)
    }
}