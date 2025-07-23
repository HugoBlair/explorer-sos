package com.example.explorersos.feature_trip.domain.use_case.alert

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.repository.AlertRepository

class GetAlert(private val alertRepository: AlertRepository) {
    suspend operator fun invoke(id: Int): Alert? {
        return alertRepository.getAlertById(id)
    }
}