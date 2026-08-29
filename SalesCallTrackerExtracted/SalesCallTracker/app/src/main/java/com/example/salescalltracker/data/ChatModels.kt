package com.example.salescalltracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ChatSenderType {
    PERSON,
    ME
}

enum class ChatMessageType {
    TEXT,
    CONTACT,
    LOCATION
}

enum class ConversationType {
    DIRECT,
    GROUP,
    CHANNEL,
    TEAM
}

data class Conversation(
    val id: String,
    val personId: String?,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val name: String = "",
    val type: String = ConversationType.DIRECT.name,
)

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderType: ChatSenderType,
    val messageType: ChatMessageType,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val attachmentUri: String? = null,
)

@Entity(
    tableName = "conversations",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [
        Index(value = ["personId"]),
        Index(value = ["type"]),
    ],
)
data class ConversationEntity(
    @PrimaryKey
    val id: String,

    val personId: String?,

    val name: String,

    val type: String,

    val lastMessage: String,

    val lastMessageTimestamp: Long,

    val unreadCount: Int,

    val isPinned: Boolean,

    val isMuted: Boolean,

    val isArchived: Boolean,
)

@Entity(
    tableName = "conversation_members",
    primaryKeys = ["conversationId", "personId"],
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["personId"]),
    ],
)
data class ConversationMemberEntity(
    val conversationId: String,
    val personId: String,
    val role: String = "MEMBER",
    val joinedAt: Long,
)

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["timestamp"]),
    ],
)
data class ChatMessageEntity(
    @PrimaryKey
    val id: String,

    val conversationId: String,

    val senderType: String,

    val messageType: String,

    val text: String,

    val timestamp: Long,

    val isRead: Boolean,

    val attachmentUri: String?,
)

fun Conversation.toEntity() =
    ConversationEntity(
        id = id,
        personId = personId,
        name = name,
        type = type,
        lastMessage = lastMessage,
        lastMessageTimestamp = lastMessageTimestamp,
        unreadCount = unreadCount,
        isPinned = isPinned,
        isMuted = isMuted,
        isArchived = isArchived,
    )

fun ConversationEntity.toDomain() =
    Conversation(
        id = id,
        personId = personId,
        lastMessage = lastMessage,
        lastMessageTimestamp = lastMessageTimestamp,
        unreadCount = unreadCount,
        isPinned = isPinned,
        isMuted = isMuted,
        isArchived = isArchived,
        name = name,
        type = type,
    )

fun ChatMessage.toEntity() =
    ChatMessageEntity(
        id = id,
        conversationId = conversationId,
        senderType = senderType.name,
        messageType = messageType.name,
        text = text,
        timestamp = timestamp,
        isRead = isRead,
        attachmentUri = attachmentUri,
    )

fun ChatMessageEntity.toDomain() =
    ChatMessage(
        id = id,
        conversationId = conversationId,
        senderType = ChatSenderType.valueOf(senderType),
        messageType = ChatMessageType.valueOf(messageType),
        text = text,
        timestamp = timestamp,
        isRead = isRead,
        attachmentUri = attachmentUri,
    )
