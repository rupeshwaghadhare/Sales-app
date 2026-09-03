package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_attachments",
    indices = [
        Index(value = ["ownerId"]),
        Index(value = ["ownerType"]),
        Index(value = ["workspaceId"]),
    ]
)
data class MediaAttachmentEntity(
    @PrimaryKey
    val id: String,
    val workspaceId: String? = null,
    val ownerId: String,
    val ownerType: String,
    val type: String,
    val uri: String? = null,
    val title: String? = null,
    val description: String? = null,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)
