package com.example.explorersos.feature_trip.domain.use_case.alert

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.repository.AlertRepository

class AddAlert(
    private val repository: AlertRepository
) {
    @Throws(Alert.InvalidAlertException::class)
    suspend operator fun invoke(alert: Alert) {

        repository.insertAlert(alert)
    }
}