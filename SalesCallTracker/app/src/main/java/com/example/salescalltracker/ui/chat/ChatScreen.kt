package com.example.salescalltracker.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private data class ChatPreview(
    val name: String,
    val message: String,
    val time: String,
    val unread: Int = 0,
    val online: Boolean = false
)

private val sampleChats = listOf(
    ChatPreview("Alex", "Let's discuss the project tomorrow.", "10:42", 2, true),
    ChatPreview("Design Team", "New design has been uploaded.", "09:18", 5),
    ChatPreview("Rahul", "Call me when you are free.", "Yesterday"),
    ChatPreview("Marketing Team", "Meeting at 4:00 PM", "Yesterday", 1),
    ChatPreview("Freelance Client", "Please check the proposal.", "Mon")
)

@Composable
fun ChatScreen(
    conversationId: String? = null,
    repository: Any? = null,
    onBack: () -> Unit = {}
) {
    var selectedChat by remember { mutableStateOf<ChatPreview?>(null) }

    if (selectedChat != null) {
        ConversationScreen(
            chat = selectedChat!!,
            onBack = { selectedChat = null }
        )
    } else {
        ChatListScreen(
            onChatClick = { selectedChat = it },
            onBack = onBack
        )
    }
}

@Composable
private fun ChatListScreen(
    onChatClick: (ChatPreview) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chats",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = {}) {
                Text("New")
            }
        }

        Text(
            text = "Personal • Teams • Clients • Groups",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(sampleChats) { chat ->
                ChatRow(
                    chat = chat,
                    onClick = { onChatClick(chat) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun ChatRow(
    chat: ChatPreview,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = chat.name.take(1).uppercase(),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = chat.time,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (chat.online) "● " + chat.message else chat.message,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unread > 0) {
                    Card(
                        shape = CircleShape
                    ) {
                        Text(
                            text = chat.unread.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationScreen(
    chat: ChatPreview,
    onBack: () -> Unit
) {
    var message by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                "Hi! How are you?",
                chat.message
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = if (chat.online) "Online" else "Last seen recently"
                )
            }

            Text("☎  ⋮")
        }

        Divider()

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message") },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(8.dp))

            Button(
                onClick = {
                    if (message.isNotBlank()) {
                        messages = messages + message
                        message = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
