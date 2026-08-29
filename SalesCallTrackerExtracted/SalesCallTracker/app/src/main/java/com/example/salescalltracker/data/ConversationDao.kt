package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    // ---------------------------------------------------------
    // CONVERSATIONS
    // ---------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(conversation: ConversationEntity)

    @Query(
        """
        SELECT * FROM conversations
        WHERE isArchived = 0
        ORDER BY isPinned DESC, lastMessageTimestamp DESC
        """
    )
    fun observeActive(): Flow<List<ConversationEntity>>

    @Query(
        """
        SELECT * FROM conversations
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: String): ConversationEntity?

    @Query(
        """
        SELECT * FROM conversations
        WHERE personId = :personId
        LIMIT 1
        """
    )
    suspend fun getByPersonId(personId: String): ConversationEntity?

    // ---------------------------------------------------------
    // CREATE / UPDATE CUSTOM GROUPS
    // ---------------------------------------------------------

    @Query(
        """
        UPDATE conversations
        SET name = :name
        WHERE id = :id
        """
    )
    suspend fun updateName(
        id: String,
        name: String
    )

    @Query(
        """
        UPDATE conversations
        SET type = :type
        WHERE id = :id
        """
    )
    suspend fun updateType(
        id: String,
        type: String
    )

    @Query(
        """
        SELECT * FROM conversations
        WHERE type = :type
        AND isArchived = 0
        ORDER BY lastMessageTimestamp DESC
        """
    )
    fun observeByType(
        type: String
    ): Flow<List<ConversationEntity>>

    // ---------------------------------------------------------
    // PIN / MUTE / ARCHIVE
    // ---------------------------------------------------------

    @Query(
        """
        UPDATE conversations
        SET isPinned = :value
        WHERE id = :id
        """
    )
    suspend fun setPinned(
        id: String,
        value: Boolean
    )

    @Query(
        """
        UPDATE conversations
        SET isMuted = :value
        WHERE id = :id
        """
    )
    suspend fun setMuted(
        id: String,
        value: Boolean
    )

    @Query(
        """
        UPDATE conversations
        SET isArchived = :value
        WHERE id = :id
        """
    )
    suspend fun setArchived(
        id: String,
        value: Boolean
    )

    // ---------------------------------------------------------
    // UNREAD
    // ---------------------------------------------------------

    @Query(
        """
        UPDATE conversations
        SET unreadCount = 0
        WHERE id = :id
        """
    )
    suspend fun markRead(id: String)

    @Query(
        """
        UPDATE conversations
        SET unreadCount = unreadCount + 1
        WHERE id = :id
        """
    )
    suspend fun markUnread(id: String)

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Query(
        """
        DELETE FROM conversations
        WHERE id = :id
        """
    )
    suspend fun deleteById(id: String)
}