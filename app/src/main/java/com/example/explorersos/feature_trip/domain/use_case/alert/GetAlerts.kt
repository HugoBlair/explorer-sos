package com.example.explorersos.feature_trip.domain.use_case.alert

import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.repository.AlertRepository
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlerts(private val repository: AlertRepository) {
    operator fun invoke(
        alertOrder: AlertOrder = AlertOrder.Date(OrderType.Descending)
    ): Flow<List<Alert>> {
        return repository.getAlerts().map { alerts ->
            when (alertOrder.orderType) {
                is OrderType.Ascending -> {
                    when (alertOrder) {
                        is AlertOrder.Date -> alerts.sortedBy { it.createdAt }
                    }
                }

                is OrderType.Descending -> {
                    when (alertOrder) {
                        is AlertOrder.Date -> alerts.sortedByDescending { it.createdAt }
                    }
                }
            }
        }
    }
}