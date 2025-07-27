package com.example.explorersos.feature_trip.domain.use_case.contact

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.repository.ContactRepository

class DeleteContact(private val repository: ContactRepository) {

    suspend operator fun invoke(contact: Contact) {
        repository.deleteContact(contact)

    }
}