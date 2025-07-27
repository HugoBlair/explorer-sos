package com.example.explorersos.feature_trip.domain.use_case.contact

import com.example.explorersos.feature_contact.domain.use_case.alert_recipient.GetContacts

data class ContactUseCases(
    val getContacts: GetContacts,
    val deleteContact: DeleteContact,
    val addContact: AddContact,
    val getContact: GetContact
)
