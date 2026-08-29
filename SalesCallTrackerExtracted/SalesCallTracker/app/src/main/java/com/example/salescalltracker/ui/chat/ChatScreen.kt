@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class,
)

package com.example.salescalltracker.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salescalltracker.data.ChatMessage
import com.example.salescalltracker.data.ChatMessageType
import com.example.salescalltracker.data.ChatSenderType
import com.example.salescalltracker.data.ActivityRepository
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun ChatScreen(
    conversationId: String,
    repository: ActivityRepository,
    onBack: () -> Unit,
    onOpenPerson: (String) -> Unit = {},
    viewModel: ChatViewModel = viewModel { ChatViewModel(repository) },
) {
    val state by viewModel.conversationState(conversationId).collectAsStateWithLifecycle()
    when (val current = state) {
        ChatConversationUiState.Loading -> Text("Loading conversation...")
        is ChatConversationUiState.Error -> Text(current.message)
        is ChatConversationUiState.Success -> {
            LaunchedEffect(current.conversation.id) { viewModel.markRead(current.conversation.id) }
            ChatConversationContent(current, viewModel, onBack, onOpenPerson)
        }
    }
}

@Composable
private fun ChatConversationContent(
    state: ChatConversationUiState.Success,
    viewModel: ChatViewModel,
    onBack: () -> Unit,
    onOpenPerson: (String) -> Unit,
) {
    var draft by remember { mutableStateOf("") }
    var showActions by remember { mutableStateOf(false) }
    var showConversationMenu by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var messageQuery by remember { mutableStateOf("") }
    var selectedMessage by remember { mutableStateOf<ChatMessage?>(null) }
    var replyingTo by remember { mutableStateOf<ChatMessage?>(null) }
    var showNoteEditor by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) listState.animateScrollToItem(state.messages.lastIndex)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        ChatAvatar(state.person.name)
                        Column {
                            Text(state.person.name, style = MaterialTheme.typography.titleMedium)
                            Text(state.person.phoneNumber ?: "CRM contact", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
                actions = {
                    TextButton(onClick = { onOpenPerson(state.person.id) }) { Text("Person") }
                    TextButton(onClick = { showSearch = !showSearch }) { Text("Search") }
                    IconButton(onClick = { dial(context, state.person.phoneNumber) }) { Text("Call") }
                    IconButton(onClick = { showConversationMenu = true }) { Text("More") }
                    DropdownMenu(expanded = showConversationMenu, onDismissRequest = { showConversationMenu = false }) {
                        DropdownMenuItem(text = { Text("Mark unread") }, onClick = { viewModel.markUnread(state.conversation.id); showConversationMenu = false })
                        DropdownMenuItem(text = { Text("Add note") }, onClick = { showNoteEditor = true; showConversationMenu = false })
                        DropdownMenuItem(text = { Text(if (state.conversation.isPinned) "Unpin" else "Pin") }, onClick = { viewModel.setPinned(state.conversation); showConversationMenu = false })
                        DropdownMenuItem(text = { Text(if (state.conversation.isMuted) "Unmute" else "Mute") }, onClick = { viewModel.setMuted(state.conversation); showConversationMenu = false })
                        DropdownMenuItem(text = { Text("Archive") }, onClick = { viewModel.archive(state.conversation); onBack() })
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (showSearch) {
                OutlinedTextField(
                    value = messageQuery,
                    onValueChange = { messageQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Search conversation") },
                    singleLine = true,
                )
            }
            val visibleMessages = state.messages.filter { message ->
                messageQuery.isBlank() || message.text.contains(messageQuery, ignoreCase = true)
            }
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.messages.isEmpty()) {
                    item {
                        Column(Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            ChatAvatar(state.person.name)
                            Text("Start a conversation", style = MaterialTheme.typography.titleMedium)
                            Text("Messages saved here stay linked to this CRM contact.")
                        }
                    }
                }
                if (state.messages.isNotEmpty() && visibleMessages.isEmpty()) {
                    item { Text("No messages match your search.", Modifier.padding(vertical = 24.dp)) }
                }
                itemsIndexed(visibleMessages, key = { _, message -> message.id }) { index, message ->
                    if (index == 0 || !isSameDay(visibleMessages[index - 1].timestamp, message.timestamp)) {
                        Text(
                            DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(message.timestamp)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                    MessageBubble(
                        message = message,
                        personName = state.person.name,
                        onLongPress = { selectedMessage = message },
                    )
                }
            }
            Box(Modifier.fillMaxWidth().padding(12.dp)) {
                Column {
                    if (replyingTo != null) {
                        Row(Modifier.fillMaxWidth().padding(bottom = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Reply to: ${replyingTo?.text}", maxLines = 1, modifier = Modifier.weight(1f))
                            TextButton(onClick = { replyingTo = null }) { Text("Cancel") }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
                        IconButton(onClick = { showActions = !showActions }) { Text("+") }
                        OutlinedTextField(
                            value = draft,
                            onValueChange = { draft = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Type a message...") },
                            shape = RoundedCornerShape(22.dp),
                            singleLine = false,
                            maxLines = 4,
                        )
                        Button(
                            enabled = draft.isNotBlank(),
                            onClick = {
                                val messageText = replyingTo?.let { "Reply to: ${it.text}\n${draft.trim()}" } ?: draft
                                viewModel.sendText(state.conversation.id, messageText)
                                draft = ""
                                replyingTo = null
                            },
                        ) { Text("Send") }
                    }
                    DropdownMenu(expanded = showActions, onDismissRequest = { showActions = false }) {
                        DropdownMenuItem(text = { Text("Contact") }, onClick = { viewModel.sendContact(state.conversation.id, state.person); showActions = false })
                        DropdownMenuItem(text = { Text("Location") }, onClick = { viewModel.sendLocationPlaceholder(state.conversation.id); showActions = false })
                    }
                }
            }
        }
    }

    selectedMessage?.let { message ->
        MessageActionDialog(
            message = message,
            onCopy = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Message", message.text))
                selectedMessage = null
            },
            onReply = { replyingTo = message; selectedMessage = null },
            onDelete = { viewModel.deleteMessage(message); selectedMessage = null },
            onDismiss = { selectedMessage = null },
        )
    }
    if (showNoteEditor) {
        NoteEditor(
            onSave = { title, description -> viewModel.addNote(state.person.id, title, description); showNoteEditor = false },
            onDismiss = { showNoteEditor = false },
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage, personName: String, onLongPress: () -> Unit) {
    val outgoing = message.senderType == ChatSenderType.ME
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (outgoing) Arrangement.End else Arrangement.Start) {
        Surface(
            modifier = Modifier.widthIn(max = 300.dp).combinedClickable(onClick = {}, onLongClick = onLongPress),
            shape = RoundedCornerShape(18.dp),
            color = if (outgoing) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp,
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (!outgoing) Text(personName, style = MaterialTheme.typography.labelMedium)
                Text(message.text, style = MaterialTheme.typography.bodyLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(message.timestamp)), style = MaterialTheme.typography.labelSmall)
                    if (outgoing) Text(if (message.isRead) "Read" else "Sent", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun MessageActionDialog(
    message: ChatMessage,
    onCopy: () -> Unit,
    onReply: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Message actions") },
        text = { Text(message.text, maxLines = 4) },
        confirmButton = { TextButton(onClick = onCopy) { Text("Copy") } },
        dismissButton = {
            Row {
                TextButton(onClick = onReply) { Text("Reply") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        },
    )
}

@Composable
private fun NoteEditor(onSave: (String, String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, singleLine = true)
                OutlinedTextField(description, { description = it }, label = { Text("Note body") }, minLines = 3)
            }
        },
        confirmButton = { TextButton(enabled = title.isNotBlank(), onClick = { onSave(title, description) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun ChatAvatar(name: String) {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer) {
        Text(name.take(1).uppercase(), Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.titleMedium)
    }
}

private fun isSameDay(firstTimestamp: Long, secondTimestamp: Long): Boolean {
    val first = Calendar.getInstance().apply { timeInMillis = firstTimestamp }
    val second = Calendar.getInstance().apply { timeInMillis = secondTimestamp }
    return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}

private fun dial(context: Context, phone: String?) {
    phone?.takeIf { it.isNotBlank() }?.let { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(it)}"))) }
}
