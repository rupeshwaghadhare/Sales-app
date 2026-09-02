package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaAttachmentDao {

    @Query("""
        SELECT * FROM media_attachments
        WHERE ownerId = :ownerId
        AND ownerType = :ownerType
        ORDER BY sortOrder ASC, createdAt ASC
    """)
    fun observeForOwner(
        ownerId: String,
        ownerType: String,
    ): Flow<List<MediaAttachmentEntity>>

    @Query("""
        SELECT * FROM media_attachments
        WHERE workspaceId = :workspaceId
        AND ownerId = :ownerId
        AND ownerType = :ownerType
        ORDER BY sortOrder ASC, createdAt ASC
    """)
    fun observeForWorkspaceOwner(
        workspaceId: String,
        ownerId: String,
        ownerType: String,
    ): Flow<List<MediaAttachmentEntity>>

    @Query("""
        SELECT * FROM media_attachments
        WHERE ownerId = :ownerId
        AND ownerType = :ownerType
        ORDER BY sortOrder ASC, createdAt ASC
    """)
    suspend fun getForOwner(
        ownerId: String,
        ownerType: String,
    ): List<MediaAttachmentEntity>

    @Query("SELECT * FROM media_attachments WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MediaAttachmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(media: MediaAttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(media: List<MediaAttachmentEntity>)

    @Delete
    suspend fun delete(media: MediaAttachmentEntity)

    @Query("DELETE FROM media_attachments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("""
        DELETE FROM media_attachments
        WHERE ownerId = :ownerId
        AND ownerType = :ownerType
    """)
    suspend fun deleteForOwner(
        ownerId: String,
        ownerType: String,
    )

    @Query("""
        DELETE FROM media_attachments
        WHERE workspaceId = :workspaceId
        AND ownerId = :ownerId
        AND ownerType = :ownerType
    """)
    suspend fun deleteForWorkspaceOwner(
        workspaceId: String,
        ownerId: String,
        ownerType: String,
    )
}
