package com.example.explorersos.feature_trip.domain.repository

import com.example.explorersos.feature_trip.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContacts(): Flow<List<Contact>>

    suspend fun getContactById(id: Int): Contact?

    suspend fun insertContact(contact: Contact)

    suspend fun deleteContact(contact: Contact)

}