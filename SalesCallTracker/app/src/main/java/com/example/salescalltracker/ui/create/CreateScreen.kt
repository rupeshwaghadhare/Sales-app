package com.example.salescalltracker.ui.create

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
fun CreateScreen(
    onBusinessClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "Create",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            "Create your digital presence and start building your work.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        CreateCard("🏪 Business", "Create a business profile, listing and business ID", onBusinessClick)
        CreateCard("🛍️ Product", "Add products with price, photos and details")
        CreateCard("🧰 Service", "Create services and show what you offer")
        CreateCard("🌐 Website", "Build a simple website for your work")
        CreateCard("🎨 Content", "Create posts and promotional content")
        CreateCard("📣 Campaign", "Create a campaign for your business")
        CreateCard("🎪 Event", "Create and promote an event")
        CreateCard("🛒 Mini Store", "Create a simple online storefront")
        CreateCard("📺 Channel", "Create your own promotional or creator channel")
    }
}

@Composable
private fun CreateCard(
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
