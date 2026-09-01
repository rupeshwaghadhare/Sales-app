package com.example.salescalltracker.model

enum class OfferingType {
    PRODUCT,
    SERVICE,
    KNOWLEDGE,
    FRANCHISE,
    CAMPAIGN,
    EVENT,
}

data class Offering(
    val id: String,
    val ownerId: String,
    val workspaceId: String? = null,
    val title: String,
    val description: String = "",
    val type: OfferingType,
    val price: Double? = null,
    val currency: String = "INR",
    val location: String? = null,
    val imageUri: String? = null,
    val contact: String? = null,
    val websiteUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
