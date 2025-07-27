package com.example.explorersos.feature_contact.domain.use_case.alert_recipient

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.repository.ContactRepository
import com.example.explorersos.feature_trip.domain.util.ContactOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContacts(private val repository: ContactRepository) {
    operator fun invoke(
        contactOrder: ContactOrder = ContactOrder.LastName(OrderType.Descending)
    ): Flow<List<Contact>> {
        return repository.getContacts().map { contacts ->
            when (contactOrder.orderType) {
                is OrderType.Ascending -> {
                    when (contactOrder) {
                        is ContactOrder.FirstName -> contacts.sortedBy { it.firstName.lowercase() }
                        is ContactOrder.LastName -> contacts.sortedBy { it.lastName.lowercase() }
                    }
                }

                is OrderType.Descending -> {
                    when (contactOrder) {
                        is ContactOrder.FirstName -> contacts.sortedByDescending { it.firstName.lowercase() }
                        is ContactOrder.LastName -> contacts.sortedByDescending { it.lastName.lowercase() }
                    }
                }
            }
        }
    }
}