package com.example.salescalltracker.ui.events

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
import com.example.salescalltracker.model.EventTimeFilter
import com.example.salescalltracker.model.PlatformEvent

@Composable
fun EventSearchScreen(
    city: String = "Pune",
    events: List<PlatformEvent> = emptyList(),
    onSearch: (String, EventTimeFilter) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember {
        mutableStateOf(EventTimeFilter.UPCOMING)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                "📅 Events",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        item {
            Text(
                "Find live and upcoming events in $city.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search events") },
                placeholder = {
                    Text("Music, workshop, startup, sports...")
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("When", style = MaterialTheme.typography.titleMedium)

                listOf(
                    EventTimeFilter.LIVE to "🔴 Live",
                    EventTimeFilter.TODAY to "Today",
                    EventTimeFilter.TOMORROW to "Tomorrow",
                    EventTimeFilter.THIS_WEEKEND to "Weekend",
                    EventTimeFilter.UPCOMING to "Upcoming",
                ).forEach { (filter, label) ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(label) },
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    onSearch(query, selectedFilter)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Search Events")
            }
        }

        if (events.isEmpty()) {
            item {
                Text(
                    "No events loaded yet. Connect an event provider to show live results.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(events.size) { index ->
                EventCard(events[index])
            }
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
private fun EventCard(event: PlatformEvent) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                event.title,
                style = MaterialTheme.typography.titleMedium,
            )

            if (event.venue.isNotBlank()) {
                Text("📍 ${event.venue}")
            }

            if (event.city.isNotBlank()) {
                Text(event.city)
            }

            if (event.category.isNotBlank()) {
                Text(
                    event.category,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (event.price != null) {
                Text("₹${event.price}")
            }

            if (event.sourceName.isNotBlank()) {
                Text(
                    "Source: ${event.sourceName}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
