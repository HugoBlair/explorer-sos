package com.example.explorersos.feature_trip.domain.use_case.contact

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.repository.ContactRepository

class AddContact(private val repository: ContactRepository) {

    @Throws(Contact.InvalidContactException::class)
    suspend operator fun invoke(contact: Contact) {
        repository.insertContact(contact)
    }
}