@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.salescalltracker.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.RelationshipType
import java.text.DateFormat
import java.util.Date

@Composable
fun ChatListScreen(
    repository: ActivityRepository,
    onOpenConversation: (String) -> Unit,
    viewModel: ChatViewModel = viewModel { ChatViewModel(repository) },
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()
    when (val current = state) {
        ChatListUiState.Loading -> Text("Loading chats...")
        is ChatListUiState.Error -> Text(current.message)
        is ChatListUiState.Success -> ChatListContent(current.conversations, repository, viewModel, onOpenConversation)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChatListContent(
    conversations: List<ConversationRow>,
    repository: ActivityRepository,
    viewModel: ChatViewModel,
    onOpenConversation: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var showNewChat by remember { mutableStateOf(false) }
    var showNewGroup by remember { mutableStateOf(false) }
    var menuConversation by remember { mutableStateOf<ConversationRow?>(null) }
    var filter by remember { mutableStateOf(ChatFilter.ALL) }
    val filtered = conversations.filter { row ->
        val matchesQuery = query.isBlank() ||
            row.person.name.contains(query, true) ||
            row.person.phoneNumber.orEmpty().contains(query, true) ||
            row.conversation.lastMessage.contains(query, true)
        val matchesFilter = when (filter) {
            ChatFilter.ALL -> true
            ChatFilter.UNREAD -> row.conversation.unreadCount > 0
            ChatFilter.GROUPS -> row.conversation.type == com.example.salescalltracker.data.ConversationType.GROUP.name
            ChatFilter.CUSTOMERS -> com.example.salescalltracker.model.RelationshipType.CUSTOMER in row.person.relationshipTypes
            ChatFilter.LEADS -> com.example.salescalltracker.model.RelationshipType.PROSPECT in row.person.relationshipTypes
            ChatFilter.PARTNERS -> com.example.salescalltracker.model.RelationshipType.BUSINESS_PARTNER in row.person.relationshipTypes
        }
        matchesQuery && matchesFilter
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chats") },
                actions = {
                    IconButton(onClick = { showNewChat = true }) { Text("Chat") }
                    IconButton(onClick = { showNewGroup = true }) { Text("Group") }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewGroup = true }) {
                Text("+")
            }
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text("Conversations", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "${filtered.size} active ${if (filtered.size == 1) "conversation" else "conversations"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search chats") },
                    singleLine = true,
                )
            }
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChatFilter.entries.forEach { option ->
                        FilterChip(
                            selected = filter == option,
                            onClick = { filter = option },
                            label = { Text(option.label) },
                        )
                    }
                }
            }
            if (filtered.isEmpty()) {
                item {
                        Column(
                            Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text("No chats yet", style = MaterialTheme.typography.titleLarge)
                            Text(
                                if (conversations.isEmpty()) "Start a conversation with a person."
                                else "No chats match this view.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            if (conversations.isEmpty()) {
                                Button(onClick = { showNewChat = true }) { Text("Start Chat") }
                            }
                        }
                }
            }
            items(filtered, key = { it.conversation.id }) { row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenConversation(row.conversation.id) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (row.conversation.unreadCount > 0) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    ),
                ) {
                    Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ChatAvatar(row.person.name)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(row.person.name, style = MaterialTheme.typography.titleMedium)
                                if (row.conversation.lastMessageTimestamp > 0) {
                                    Text(DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(row.conversation.lastMessageTimestamp)), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            if (!row.person.phoneNumber.isNullOrBlank()) {
                                Text(row.person.phoneNumber, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(row.person.relationshipTypes.joinToString(" â€¢ ") { it.label() }, style = MaterialTheme.typography.labelMedium)
                            Text(row.conversation.lastMessage.ifBlank { "No messages yet" }, maxLines = 1)
                        }
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                            if (row.conversation.unreadCount > 0) {
                                Badge { Text(row.conversation.unreadCount.toString()) }
                            }
                            TextButton(onClick = { menuConversation = row }) { Text("More") }
                        }
                    }
                    if (row.conversation.isPinned || row.conversation.isMuted) {
                        Row(
                            Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            if (row.conversation.isPinned) StatusPill("Pinned")
                            if (row.conversation.isMuted) StatusPill("Muted")
                        }
                    }
                }
            }
        }
    }
    menuConversation?.let { row ->
        AlertDialog(
            onDismissRequest = { menuConversation = null },
            title = { Text(row.person.name) },
            text = { Text("${if (row.conversation.isPinned) "Pinned" else "Not pinned"} â€¢ ${if (row.conversation.isMuted) "Muted" else "Notifications on"}") },
            confirmButton = {
                TextButton(onClick = { viewModel.setPinned(row.conversation); menuConversation = null }) { Text("Pin / Unpin") }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            if (row.conversation.unreadCount > 0) viewModel.markRead(row.conversation.id)
                            else viewModel.markUnread(row.conversation.id)
                            menuConversation = null
                        },
                    ) { Text(if (row.conversation.unreadCount > 0) "Mark read" else "Mark unread") }
                    TextButton(onClick = { viewModel.setMuted(row.conversation); menuConversation = null }) { Text("Mute") }
                    TextButton(onClick = { viewModel.archive(row.conversation); menuConversation = null }) { Text("Archive") }
                }
            },
        )
    }
    if (showNewChat) NewChatDialog(repository, viewModel, onOpenConversation) { showNewChat = false }
    if (showNewGroup) NewGroupDialog(repository, viewModel, onOpenConversation) { showNewGroup = false }
}

@Composable
private fun ChatAvatar(name: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Text(
            text = name.take(1).uppercase(),
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 11.dp),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun StatusPill(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Text(text, Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall)
    }
}

private enum class ChatFilter(val label: String) {
    ALL("All"),
    UNREAD("Unread"),
    GROUPS("Groups"),
    CUSTOMERS("Customers"),
    LEADS("Leads"),
    PARTNERS("Partners"),
}

private fun RelationshipType.label(): String =
    name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }

@Composable
private fun NewChatDialog(
    repository: ActivityRepository,
    viewModel: ChatViewModel,
    onOpenConversation: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val peopleState by repository.observePeople().collectAsStateWithLifecycle(initialValue = emptyList())
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New chat") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (peopleState.isEmpty()) Text("Add a person before starting a chat.")
                peopleState.forEach { person ->
                    TextButton(
                        onClick = { viewModel.openOrCreate(person.id, onOpenConversation); onDismiss() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(person.name) }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Close") } },
    )
}

@Composable
private fun NewGroupDialog(
    repository: ActivityRepository,
    viewModel: ChatViewModel,
    onOpenConversation: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val peopleState by repository.observePeople().collectAsStateWithLifecycle(initialValue = emptyList())
    var groupName by remember { mutableStateOf("") }
    val selected = remember { mutableStateOf(setOf<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Group") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Group name") },
                    placeholder = { Text("Enter group name") },
                    singleLine = true,
                )
                Text("Select members", style = MaterialTheme.typography.titleSmall)
                if (peopleState.isEmpty()) {
                    Text("Add people first so you can create a group.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        peopleState.forEach { person ->
                            val checked = person.id in selected.value
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth().clickable { 
                                    selected.value = if (checked) selected.value - person.id else selected.value + person.id
                                },
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        selected.value = if (isChecked) selected.value + person.id else selected.value - person.id
                                    },
                                )
                                Text(person.name)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = groupName.isNotBlank() && selected.value.isNotEmpty(),
                onClick = {
                    viewModel.createGroup(groupName, selected.value.toList()) { conversationId ->
                        onOpenConversation(conversationId)
                    }
                    onDismiss()
                },
            ) {
                Text("Create Group")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}





