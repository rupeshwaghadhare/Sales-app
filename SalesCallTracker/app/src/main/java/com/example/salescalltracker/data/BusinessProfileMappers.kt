package com.example.salescalltracker.data

import com.example.salescalltracker.model.BusinessProfile

fun BusinessProfile.toEntity(): BusinessProfileEntity =
    BusinessProfileEntity(
        id = id,
        businessName = businessName,
        category = category,
        phone = phone,
        whatsapp = whatsapp,
        location = location,
        description = description,
        logoUri = logoUri,
        websiteUrl = websiteUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun BusinessProfileEntity.toDomain(): BusinessProfile =
    BusinessProfile(
        id = id,
        businessName = businessName,
        category = category,
        phone = phone,
        whatsapp = whatsapp,
        location = location,
        description = description,
        logoUri = logoUri,
        websiteUrl = websiteUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
