package com.samay.app.data.contact

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDao) {
    val contact: Flow<Contact?> = dao.getContact()

    suspend fun saveContact(name: String, phone: String) {
        dao.insertContact(Contact(name = name, phone = phone))
    }

    suspend fun clearContact() {
        dao.deleteContact()
    }
}
