package com.example.salescalltracker.model

data class BusinessProfile(
    val id: String,
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
