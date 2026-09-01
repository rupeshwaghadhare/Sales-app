package com.example.salescalltracker.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CreateOption(
    val id: String,
    val title: String,
    val description: String,
)

private val createOptions = listOf(
    CreateOption(
        "BUSINESS",
        "🏪 Business",
        "Create a business profile, digital identity and business ID.",
    ),
    CreateOption(
        "PRODUCT",
        "🛍️ Product",
        "Sell a new, used, physical or digital product.",
    ),
    CreateOption(
        "SERVICE",
        "🧰 Service",
        "Offer your professional skills, freelance or local services.",
    ),
    CreateOption(
        "KNOWLEDGE",
        "📚 Knowledge",
        "Share notes, courses, workshops, tutorials and knowledge.",
    ),
    CreateOption(
        "FRANCHISE",
        "🏷️ Franchise",
        "List a franchise opportunity for interested partners.",
    ),
    CreateOption(
        "CAMPAIGN",
        "📣 Campaign",
        "Create a campaign and collaborate with creators or marketers.",
    ),
    CreateOption(
        "EVENT",
        "🎪 Event",
        "Create and promote an event with location and details.",
    ),
    CreateOption(
        "WEBSITE",
        "🌐 Website",
        "Build a simple public website for yourself or a business.",
    ),
    CreateOption(
        "MINI_APP",
        "📱 Mini App",
        "Create a simple app-like experience without traditional coding.",
    ),
    CreateOption(
        "CHANNEL",
        "📺 Channel",
        "Create your own creator, education or business channel.",
    ),
    CreateOption(
        "CONTENT",
        "🎨 Content",
        "Create posts, stories, promotional content and media.",
    ),
)

@Composable
fun CreateScreen(
    onBusinessClick: () -> Unit = {},
    onOfferingClick: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                "Create",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        item {
            Text(
                "Create something for yourself or on behalf of another person or business.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            Text(
                "What do you want to create?",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(createOptions.size) { index ->
            val option = createOptions[index]

            CreateCard(
                title = option.title,
                description = option.description,
                onClick = {
                    if (option.id == "BUSINESS") {
                        onBusinessClick()
                    } else {
                        onOfferingClick(option.id)
                    }
                },
            )
        }
    }
}

@Composable
private fun CreateCard(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
