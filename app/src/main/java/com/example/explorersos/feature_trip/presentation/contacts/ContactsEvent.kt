package com.example.explorersos.feature_trip.presentation.contacts

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.util.ContactOrder

sealed class ContactsEvent {
    data class Order(val contactOrder: ContactOrder) : ContactsEvent()
    data class DeleteContact(val contact: Contact) : ContactsEvent()
    object RestoreContact : ContactsEvent()
    object ToggleOrderSection : ContactsEvent()
}