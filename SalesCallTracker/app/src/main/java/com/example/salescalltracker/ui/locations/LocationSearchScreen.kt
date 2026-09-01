package com.example.salescalltracker.ui.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salescalltracker.model.LocationSearchType
import com.example.salescalltracker.model.PlatformLocation

@Composable
fun LocationSearchScreen(
    locations: List<PlatformLocation> = emptyList(),
    onSearch: (String, LocationSearchType) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    var selectedType by remember {
        mutableStateOf(LocationSearchType.ALL)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                "📍 Locations",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        item {
            Text(
                "Find businesses, venues, services and places around you.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search location") },
                placeholder = {
                    Text("Cafe, hotel, gym, shop, venue...")
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Category", style = MaterialTheme.typography.titleMedium)

                listOf(
                    LocationSearchType.ALL to "Everything",
                    LocationSearchType.BUSINESS to "Businesses",
                    LocationSearchType.VENUE to "Venues",
                    LocationSearchType.EVENT to "Events",
                    LocationSearchType.SERVICE to "Services",
                ).forEach { (type, label) ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(label) },
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    onSearch(query, selectedType)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Search Nearby")
            }
        }

        item {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        "🗺️ Map",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        "Google Maps/Places integration will provide map, nearby places, distance and directions.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        items(locations.size) { index ->
            LocationCard(locations[index])
        }

        item {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun LocationCard(location: PlatformLocation) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                location.name,
                style = MaterialTheme.typography.titleMedium,
            )

            if (location.category.isNotBlank()) {
                Text(location.category)
            }

            if (location.address.isNotBlank()) {
                Text("📍 ${location.address}")
            }
        }
    }
}
