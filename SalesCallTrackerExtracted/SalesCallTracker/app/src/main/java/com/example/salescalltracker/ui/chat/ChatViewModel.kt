package com.example.salescalltracker.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.data.ChatMessage
import com.example.salescalltracker.data.ChatMessageType
import com.example.salescalltracker.data.ChatSenderType
import com.example.salescalltracker.data.Conversation
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(private val repository: ActivityRepository) : ViewModel() {
    val listState: StateFlow<ChatListUiState> = combine(
        repository.observeConversations(),
        repository.observePeople(),
    ) { conversations, people ->
        ChatListUiState.Success(
            conversations.map { conversation ->
                val person = people.firstOrNull { it.id == conversation.personId }
                    ?: Person(
                        id = conversation.id,
                        name = conversation.name,
                        phoneNumber = null,
                        relationshipTypes = emptySet(),
                    )
                ConversationRow(conversation, person)
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatListUiState.Loading)

    fun conversationState(conversationId: String): StateFlow<ChatConversationUiState> = combine(
        repository.observeConversations(),
        repository.observePeople(),
        repository.observeMessages(conversationId),
    ) { conversations, people, messages ->
        val conversation = conversations.firstOrNull { it.id == conversationId }
            ?: return@combine ChatConversationUiState.Error("Conversation unavailable")
        val person = people.firstOrNull { it.id == conversation.personId }
            ?: Person(
                id = conversation.personId ?: conversation.id,
                name = conversation.name,
                phoneNumber = null,
                relationshipTypes = emptySet(),
            )
        ChatConversationUiState.Success(conversation, person, messages)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatConversationUiState.Loading)

    fun openOrCreate(personId: String, onReady: (String) -> Unit) = viewModelScope.launch {
        onReady(repository.getOrCreateConversation(personId).id)
    }

    fun createGroup(name: String, memberIds: List<String>, onReady: (String) -> Unit = {}) = viewModelScope.launch {
        val group = repository.createGroup(name, memberIds)
        onReady(group.id)
    }

    fun sendText(conversationId: String, text: String) {
        if (text.isBlank()) return
        send(conversationId, ChatMessageType.TEXT, text.trim())
    }

    fun sendContact(conversationId: String, person: Person) = send(conversationId, ChatMessageType.CONTACT, person.name)

    fun sendLocationPlaceholder(conversationId: String) = send(conversationId, ChatMessageType.LOCATION, "Location sharing is available from the device")

    fun addNote(personId: String, title: String, description: String) = viewModelScope.launch {
        if (title.isBlank()) return@launch
        repository.upsertActivity(
            com.example.salescalltracker.model.Activity(
                id = UUID.randomUUID().toString(),
                personId = personId,
                type = ActivityType.NOTE,
                title = title.trim(),
                description = description.trim(),
                timestamp = System.currentTimeMillis(),
            ),
        )
    }

    fun deleteMessage(message: ChatMessage) = viewModelScope.launch { repository.deleteMessage(message) }

    private fun send(conversationId: String, type: ChatMessageType, text: String) = viewModelScope.launch {
        repository.sendMessage(
            ChatMessage(UUID.randomUUID().toString(), conversationId, ChatSenderType.ME, type, text, System.currentTimeMillis(), true),
        )
    }

    fun markRead(id: String) = viewModelScope.launch { repository.markConversationRead(id) }
    fun markUnread(id: String) = viewModelScope.launch { repository.markConversationUnread(id) }
    fun setPinned(conversation: Conversation) = viewModelScope.launch { repository.setConversationPinned(conversation.id, !conversation.isPinned) }
    fun setMuted(conversation: Conversation) = viewModelScope.launch { repository.setConversationMuted(conversation.id, !conversation.isMuted) }
    fun archive(conversation: Conversation) = viewModelScope.launch { repository.setConversationArchived(conversation.id, true) }
}
