package com.example.explorersos.feature_trip.presentation.contacts

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.util.ContactOrder
import com.example.explorersos.feature_trip.domain.util.OrderType

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val contactOrder: ContactOrder = ContactOrder.LastName(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)