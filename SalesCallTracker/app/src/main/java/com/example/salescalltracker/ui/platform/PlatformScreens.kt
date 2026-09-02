package com.example.salescalltracker.ui.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MarketplaceScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "🛒 Marketplace",
        "Discover products, services, businesses and opportunities.",
        listOf(
            "Products" to "Discover physical and digital products.",
            "Services" to "Find freelancers and professional services.",
            "Businesses" to "Discover local and online businesses.",
            "Franchise" to "Explore franchise opportunities."
        ),
        onBack
    )
}

@Composable
fun CampaignsScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "📣 Campaigns",
        "Discover campaigns from brands and businesses.",
        listOf(
            "Local Campaigns" to "Find campaigns running near you.",
            "Creator Campaigns" to "Find brand collaboration opportunities.",
            "Promote a Business" to "Create promotional campaigns.",
            "Track Results" to "Track campaign performance."
        ),
        onBack
    )
}

@Composable
fun ServicesScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "🧰 Services",
        "Discover and offer professional services.",
        listOf(
            "My Services" to "Create and manage your services.",
            "Find Services" to "Find freelancers and professionals.",
            "Projects" to "Discover work and collaboration opportunities."
        ),
        onBack
    )
}

@Composable
fun ProductsScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "🛍️ Products",
        "Create, manage and discover products.",
        listOf(
            "My Products" to "Manage products and prices.",
            "Digital Products" to "Sell PDFs, templates and courses.",
            "Used Products" to "List products you no longer need."
        ),
        onBack
    )
}

@Composable
fun WebsiteScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "🌐 Website",
        "Create a simple online presence.",
        listOf(
            "Create Website" to "Build a website using templates.",
            "Business Page" to "Show your business, products and services.",
            "Share Link" to "Share your public page."
        ),
        onBack
    )
}

@Composable
fun LocationsScreen(onBack: () -> Unit = {}) {
    PlatformPage(
        "📍 Locations & Events",
        "Discover local businesses, places and events.",
        listOf(
            "Nearby Businesses" to "Find businesses around your location.",
            "Events" to "Discover local events.",
            "My Locations" to "Manage business and event locations."
        ),
        onBack
    )
}

@Composable
private fun PlatformPage(
    title: String,
    description: String,
    actions: List<Pair<String, String>>,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentPadding = PaddingValues(bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(actions.size) { index ->
            val action = actions[index]

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        action.first,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        action.second,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

