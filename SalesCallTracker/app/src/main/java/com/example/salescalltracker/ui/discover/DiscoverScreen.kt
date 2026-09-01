package com.example.salescalltracker.ui.discover

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
fun DiscoverScreen(
    onMarketplaceClick: () -> Unit = {},
    onCampaignsClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "Discover",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            "Find businesses, products, services, campaigns and opportunities around you.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        DiscoverCard("🛒 Marketplace", "Buy and sell products and services", onMarketplaceClick)
        DiscoverCard("📣 Brand Campaigns", "Discover campaigns from businesses", onCampaignsClick)
        DiscoverCard("🏪 Local Businesses", "Explore businesses and shops near you")
        DiscoverCard("🎯 Opportunities", "Find projects, jobs and collaborations")
        DiscoverCard("🎪 Events", "Discover business and community events")
        DiscoverCard("🛍️ Products & Services", "Explore offers from independent sellers")
    }
}

@Composable
private fun DiscoverCard(
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
