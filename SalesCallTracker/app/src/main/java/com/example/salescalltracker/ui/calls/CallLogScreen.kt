@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.salescalltracker.ui.calls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salescalltracker.SalesCall

@Composable
fun CallLogScreen(
    calls: List<SalesCall>,
    onLoadCalls: () -> Unit,
    onViewDetails: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    val groupedCalls = calls
        .filter {
            searchText.isBlank() ||
                it.number.contains(searchText, ignoreCase = true) ||
                it.type.contains(searchText, ignoreCase = true)
        }
        .groupBy { it.number }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Call Log") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your calls",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "${calls.size} call records | ${groupedCalls.size} numbers",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Search phone number or type")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onLoadCalls,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Refresh Call Logs")
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    groupedCalls.entries.toList(),
                    key = { it.key }
                ) { entry ->
                    GroupedCallCard(
                        number = entry.key,
                        calls = entry.value,
                        onViewDetails = onViewDetails
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupedCallCard(
    number: String,
    calls: List<SalesCall>,
    onViewDetails: (String) -> Unit
) {
    val latest = calls.lastOrNull()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Text(
                text = number,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${calls.size} calls",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            latest?.let {
                Text("Last call: ${it.date}")
                Text("Last type: ${it.type}")
                Text("Last duration: ${it.duration}")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // Phone action will be connected next.
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Call")
                }

                Button(
                    onClick = {
                        // WhatsApp action will be connected next.
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("WhatsApp")
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    onViewDetails(number)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Details (${calls.size} calls)")
            }
        }
    }
}
