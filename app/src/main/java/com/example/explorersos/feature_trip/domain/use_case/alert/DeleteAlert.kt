package com.example.explorersos.feature_trip.domain.use_case.alert

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.repository.AlertRepository

class DeleteAlert(private val alertRepository: AlertRepository) {
    suspend operator fun invoke(alert: Alert) {
        alertRepository.deleteAlert(alert)
    }
}
