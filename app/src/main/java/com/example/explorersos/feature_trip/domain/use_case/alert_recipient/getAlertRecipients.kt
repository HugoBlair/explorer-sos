package com.example.explorersos.feature_alertrecipient.domain.use_case.alert_recipient

import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.repository.AlertRecipientRepository
import com.example.explorersos.feature_trip.domain.util.AlertRecipientOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlertRecipients(private val repository: AlertRecipientRepository) {
    operator fun invoke(
        alertRecipientOrder: AlertRecipientOrder = AlertRecipientOrder.LastName(OrderType.Descending)
    ): Flow<List<AlertRecipient>> {
        return repository.getAlertRecipients().map { alertRecipients ->
            when (alertRecipientOrder.orderType) {
                is OrderType.Ascending -> {
                    when (alertRecipientOrder) {
                        is AlertRecipientOrder.FirstName -> alertRecipients.sortedBy { it.firstName.lowercase() }
                        is AlertRecipientOrder.LastName -> alertRecipients.sortedBy { it.lastName.lowercase() }
                    }
                }

                is OrderType.Descending -> {
                    when (alertRecipientOrder) {
                        is AlertRecipientOrder.FirstName -> alertRecipients.sortedByDescending { it.firstName.lowercase() }
                        is AlertRecipientOrder.LastName -> alertRecipients.sortedByDescending { it.lastName.lowercase() }
                    }
                }
            }
        }
    }
}