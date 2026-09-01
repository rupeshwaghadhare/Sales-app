package com.example.salescalltracker.ui.connect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConnectScreen(
    onChatsClick: () -> Unit = {},
    onPeopleClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "Connect",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            "Communicate and collaborate with people, businesses and creators.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        ConnectCard("💬 Chats", "Message customers, businesses and collaborators", onChatsClick)
        ConnectCard("👥 People", "Manage your contacts and relationships", onPeopleClick)
        ConnectCard("📞 Calls", "Track calls and follow-ups", onCallsClick)
        ConnectCard("🤝 Collaborations", "Work with creators, freelancers and brands")
        ConnectCard("👨‍👩‍👧 Groups", "Create groups for teams and communities")
    }
}

@Composable
private fun ConnectCard(
    title: String,
    description: String,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
