package com.example.explorersos.feature_trip.presentation.contacts

import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.util.AlertRecipientOrder
import com.example.explorersos.feature_trip.domain.util.OrderType

data class ContactsState(
    val contacts: List<AlertRecipient> = emptyList(),
    val contactOrder: AlertRecipientOrder = AlertRecipientOrder.LastName(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)