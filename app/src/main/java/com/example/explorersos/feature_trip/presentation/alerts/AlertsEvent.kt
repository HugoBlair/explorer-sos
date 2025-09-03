package com.example.explorersos.feature_trip.presentation.alerts

import com.example.explorersos.feature_trip.domain.model.Alert

sealed class AlertsEvent {
    data class DeleteAlert(val alert: Alert) : AlertsEvent()
    object RestoreAlert : AlertsEvent()
}