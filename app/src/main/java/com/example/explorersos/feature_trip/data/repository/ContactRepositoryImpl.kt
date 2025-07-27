package com.example.explorersos.feature_trip.data.repository

import com.example.explorersos.feature_trip.data.data_source.ContactDao
import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(private val contactDao: ContactDao) :
    ContactRepository {
    override fun getContacts(): Flow<List<Contact>> {
        return contactDao.getContacts()
    }

    override suspend fun getContactById(id: Int): Contact? {
        return contactDao.getContactById(id)
    }

    override suspend fun insertContact(contact: Contact) {
        return contactDao.insertContact(contact)
    }

    override suspend fun deleteContact(contact: Contact) {
        return contactDao.deleteContact(contact)
    }


}