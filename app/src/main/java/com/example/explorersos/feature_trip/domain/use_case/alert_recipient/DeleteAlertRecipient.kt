package com.example.explorersos.feature_trip.domain.use_case.alert_recipient

import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.repository.AlertRecipientRepository

class DeleteAlertRecipient(private val repository: AlertRecipientRepository) {

    suspend operator fun invoke(alertRecipient: AlertRecipient) {
        repository.deleteAlertRecipient(alertRecipient)

    }
}