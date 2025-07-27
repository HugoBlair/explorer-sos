package com.example.explorersos.feature_trip.domain.use_case.contact

import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.repository.ContactRepository

class GetContact(private val repository: ContactRepository) {
    suspend operator fun invoke(id: Int): Contact? {
        return repository.getContactById(id)
    }
}