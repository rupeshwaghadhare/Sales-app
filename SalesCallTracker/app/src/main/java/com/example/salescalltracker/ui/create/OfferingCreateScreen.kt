package com.example.salescalltracker.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OfferingCreateScreen(
    type: String,
    onBack: () -> Unit = {},
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val displayType = when (type) {
        "PRODUCT" -> "🛍️ Product"
        "SERVICE" -> "🧰 Service"
        "KNOWLEDGE" -> "📚 Knowledge"
        "FRANCHISE" -> "🏷️ Franchise"
        "CAMPAIGN" -> "📣 Campaign"
        "EVENT" -> "🎪 Event"
        else -> "Create"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            displayType,
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            "Create this for yourself or on behalf of another person or business.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Name / Title") },
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth(),
        )

        if (type == "PRODUCT" ||
            type == "SERVICE" ||
            type == "KNOWLEDGE" ||
            type == "FRANCHISE"
        ) {
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (₹)") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Button(
            onClick = {
                // Saving will be connected to Room in the next step.
            },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save Draft")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back")
        }
    }
}
