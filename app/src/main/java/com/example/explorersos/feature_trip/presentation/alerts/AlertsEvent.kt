package com.example.explorersos.feature_trip.presentation.alerts

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.util.AlertOrder

sealed class AlertsEvent {
    data class Order(val alertOrder: AlertOrder) : AlertsEvent()
    data class DeleteAlert(val alert: Alert) : AlertsEvent()
    object RestoreAlert : AlertsEvent()
    object ToggleOrderSection : AlertsEvent()
}