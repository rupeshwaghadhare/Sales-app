package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.salescalltracker.model.Offering
import com.example.salescalltracker.model.OfferingType

@Entity(
    tableName = "offerings",
    indices = [
        Index(value = ["ownerId"]),
        Index(value = ["workspaceId"]),
        Index(value = ["type"]),
        Index(value = ["location"]),
        Index(value = ["isActive"]),
    ],
)
data class OfferingEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val workspaceId: String?,
    val title: String,
    val description: String,
    val type: String,
    val price: Double?,
    val currency: String,
    val location: String?,
    val imageUri: String?,
    val contact: String?,
    val websiteUrl: String?,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)

fun Offering.toEntity(): OfferingEntity =
    OfferingEntity(
        id = id,
        ownerId = ownerId,
        workspaceId = workspaceId,
        title = title,
        description = description,
        type = type.name,
        price = price,
        currency = currency,
        location = location,
        imageUri = imageUri,
        contact = contact,
        websiteUrl = websiteUrl,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun OfferingEntity.toDomain(): Offering =
    Offering(
        id = id,
        ownerId = ownerId,
        workspaceId = workspaceId,
        title = title,
        description = description,
        type = OfferingType.valueOf(type),
        price = price,
        currency = currency,
        location = location,
        imageUri = imageUri,
        contact = contact,
        websiteUrl = websiteUrl,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
