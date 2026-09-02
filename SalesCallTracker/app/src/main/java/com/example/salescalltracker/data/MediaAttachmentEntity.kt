package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_attachments"
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
