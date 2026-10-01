package com.samay.app.data.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey val id: Int = 1, // Only 1 trusted person for Free tier (MVP)
    val name: String,
    val phone: String
)
