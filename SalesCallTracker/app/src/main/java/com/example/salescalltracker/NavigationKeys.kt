package com.example.salescalltracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object Discover : NavKey
@Serializable data object Create : NavKey
@Serializable data object Connect : NavKey
@Serializable data object Chats : NavKey
@Serializable data object Profile : NavKey

@Serializable data object People : NavKey
@Serializable data object Calls : NavKey
@Serializable data object More : NavKey
@Serializable data object Earn : NavKey

@Serializable data object BusinessProfile : NavKey
@Serializable data object Marketplace : NavKey
@Serializable data object Campaigns : NavKey
@Serializable data object Services : NavKey
@Serializable data object Products : NavKey
@Serializable data object Website : NavKey
@Serializable data object Locations : NavKey
@Serializable data object EventSearch : NavKey
@Serializable data class CreateOffering(val type: String) : NavKey

@Serializable data class ChatConversation(
    val conversationId: String,
) : NavKey

@Serializable data class CallDetails(
    val number: String,
) : NavKey



