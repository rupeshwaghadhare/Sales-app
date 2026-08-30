@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.salescalltracker.ui.calls

import android.content.Intent
import android.net.Uri

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    onLoadCalls: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val filteredCalls = calls.filter {
        searchText.isBlank() ||
            it.number.contains(searchText, ignoreCase = true) ||
            it.type.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Call Log")
                },
                actions = {
                    IconButton(onClick = onLoadCalls) {
                        Text("↻")
                    }
                }
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
                text = "${calls.size} calls found",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                   Text("â†»")
                },
                placeholder = {
                    Text("Search phone number or type")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onLoadCalls,
                modifier = Modifier.fillMaxWidth()
            ) {
               Text("ðŸ”")

                Spacer(modifier = Modifier.padding(4.dp))

                Text("Refresh Call Logs")
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCalls) { call ->
                    CallCard(call)
                }
            }
        }
    }
}

@Composable
private fun CallCard(
    call: SalesCall
) {
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
                text = call.number,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${call.type} â€¢ ${call.date}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Duration: ${call.duration}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        // Call action will be connected to Android phone later.
                    },
                    modifier = Modifier.weight(1f)
                ) {
                   Text("📞")
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text("Call")
                }

                Button(
                    onClick = {
                        openWhatsApp(call.number)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("WhatsApp")
                }

                IconButton(
                    onClick = {
                        // Save-contact flow will be added next.
                    }
                ) {
                    Text("ðŸ‘¤")
                }
            }
        }
    }
}

private fun openWhatsApp(
    number: String
) {
    val cleanNumber = number
        .filter { it.isDigit() || it == '+' }
        .replace("+", "")

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://wa.me/$cleanNumber")
    )

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    // This helper is intentionally isolated.
    // WhatsApp integration will be connected from an Android Context
    // in the next step.
}
