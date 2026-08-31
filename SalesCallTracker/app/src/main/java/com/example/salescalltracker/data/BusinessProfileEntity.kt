package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_profiles")
data class BusinessProfileEntity(
    @PrimaryKey val id: String,
    val businessName: String,
    val category: String,
    val phone: String?,
    val whatsapp: String?,
    val location: String?,
    val description: String?,
    val logoUri: String?,
    val websiteUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
