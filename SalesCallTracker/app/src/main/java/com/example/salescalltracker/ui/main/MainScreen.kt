package com.example.salescalltracker.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.example.salescalltracker.BusinessProfile
import com.example.salescalltracker.Campaigns
import com.example.salescalltracker.Create
import com.example.salescalltracker.Discover
import com.example.salescalltracker.Locations
import com.example.salescalltracker.Marketplace
import com.example.salescalltracker.Products
import com.example.salescalltracker.Services
import com.example.salescalltracker.Website
import com.example.salescalltracker.data.ActivityRepository

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    repository: ActivityRepository,
    modifier: Modifier = Modifier,
) {
    HomePlatform(
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

@Composable
private fun HomePlatform(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 20.dp,
            end = 16.dp,
            bottom = 100.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    "Welcome 👋",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    "Explore. Create. Connect.",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Text(
                    "Discover businesses, products, services, campaigns, creators and opportunities around you.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "🚀 Build your presence",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        "Create your own business, service, product, channel or professional identity.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Button(
                        onClick = { onItemClick(Create) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Create something")
                    }
                }
            }
        }

        item {
            Text(
                "Explore",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PlatformCard(
                    "🔎",
                    "Discover",
                    "Find people, brands and opportunities",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Discover)
                }

                PlatformCard(
                    "🛒",
                    "Marketplace",
                    "Products and services",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Marketplace)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PlatformCard(
                    "📣",
                    "Campaigns",
                    "Brand campaigns and collaborations",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Campaigns)
                }

                PlatformCard(
                    "🧰",
                    "Services",
                    "Freelancers and professionals",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Services)
                }
            }
        }

        item {
            Text(
                "Around you",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            OutlinedCard(
                onClick = { onItemClick(Locations) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        "📍 Local businesses & events",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        "Find shops, businesses, events, offers and campaigns near your location.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item {
            Text(
                "What people are building",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(
            listOf(
                "🏪 Local Business" to "Build a digital identity for your shop or business.",
                "💻 Freelancer" to "Publish your services and find customers.",
                "🎨 Creator Channel" to "Create a channel and collaborate with brands.",
                "🎓 Knowledge" to "Share what you know and monetize your knowledge.",
                "🛍️ Products" to "Sell new or used physical or digital products.",
                "🏢 Franchise" to "Promote franchise opportunities for your brand.",
            )
        ) { item ->
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClick(Create) },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        item.first,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        item.second,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item {
            Text(
                "Your digital presence",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PlatformCard(
                    "🏢",
                    "My Business",
                    "Manage your identity",
                    Modifier.weight(1f),
                ) {
                    onItemClick(BusinessProfile)
                }

                PlatformCard(
                    "🌐",
                    "Website",
                    "Build your online presence",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Website)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PlatformCard(
                    "🛍️",
                    "Products",
                    "Manage what you sell",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Products)
                }

                PlatformCard(
                    "📍",
                    "Locations",
                    "Manage places and events",
                    Modifier.weight(1f),
                ) {
                    onItemClick(Locations)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                "Your workspace",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Text(
                "Sales, calls, people, chats and follow-ups are available inside your workspace when you need them.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PlatformCard(
    emoji: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                emoji,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
