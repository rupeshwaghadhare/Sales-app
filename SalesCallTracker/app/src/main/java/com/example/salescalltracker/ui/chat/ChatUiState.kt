package com.example.salescalltracker.ui.chat

import com.example.salescalltracker.data.ChatMessage
import com.example.salescalltracker.data.Conversation
import com.example.salescalltracker.model.Person

data class ConversationRow(
    val conversation: Conversation,
    val person: Person,
)

sealed interface ChatListUiState {
    data object Loading : ChatListUiState
    data class Success(val conversations: List<ConversationRow>) : ChatListUiState
    data class Error(val message: String) : ChatListUiState
}

sealed interface ChatConversationUiState {
    data object Loading : ChatConversationUiState
    data class Success(val conversation: Conversation, val person: Person, val messages: List<ChatMessage>) : ChatConversationUiState
    data class Error(val message: String) : ChatConversationUiState
}
