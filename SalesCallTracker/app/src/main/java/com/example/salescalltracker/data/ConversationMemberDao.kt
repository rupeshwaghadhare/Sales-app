package com.example.salescalltracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationMemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMember(member: ConversationMemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMembers(
        members: List<ConversationMemberEntity>
    )

    @Query("""
        SELECT * FROM conversation_members
        WHERE conversationId = :conversationId
        ORDER BY joinedAt ASC
    """)
    fun observeMembers(
        conversationId: String
    ): Flow<List<ConversationMemberEntity>>

    @Query("""
        SELECT * FROM conversation_members
        WHERE conversationId = :conversationId
        AND personId = :personId
        LIMIT 1
    """)
    suspend fun getMember(
        conversationId: String,
        personId: String
    ): ConversationMemberEntity?

    @Query("""
        UPDATE conversation_members
        SET role = :role
        WHERE conversationId = :conversationId
        AND personId = :personId
    """)
    suspend fun updateRole(
        conversationId: String,
        personId: String,
        role: String
    )

    @Query("""
        DELETE FROM conversation_members
        WHERE conversationId = :conversationId
        AND personId = :personId
    """)
    suspend fun removeMember(
        conversationId: String,
        personId: String
    )

    @Query("""
        DELETE FROM conversation_members
        WHERE conversationId = :conversationId
    """)
    suspend fun removeAllMembers(
        conversationId: String
    )
}