package com.example.explorersos.feature_trip.presentation.alerts

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.domain.util.OrderType

data class AlertsState(
    val alerts: List<Alert> = emptyList(),
    val alertOrder: AlertOrder = AlertOrder.Date(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)