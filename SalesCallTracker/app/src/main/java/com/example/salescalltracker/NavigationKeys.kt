package com.example.salescalltracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object People : NavKey
@Serializable data object Calls : NavKey
@Serializable data object Chats : NavKey
@Serializable data class ChatConversation(val conversationId: String) : NavKey
@Serializable data object More : NavKey
