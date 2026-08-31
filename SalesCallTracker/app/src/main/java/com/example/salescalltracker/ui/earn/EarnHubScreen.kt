package com.example.salescalltracker.ui.earn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class EarningOpportunity(
    val title: String,
    val category: String,
    val location: String,
    val earning: String,
    val action: String,
)

@Composable
fun EarnHubScreen(
    onOpportunitySelected: (EarningOpportunity) -> Unit = {}
) {
    val opportunities = listOf(
        EarningOpportunity("Website Client", "Service", "Pune", "Earn ₹3,000", "Refer"),
        EarningOpportunity("Digital Marketing Project", "Service", "Local Business", "Earn ₹5,000", "Apply"),
        EarningOpportunity("Hotel Referral", "Referral", "Pune", "Commission ₹500", "Refer"),
        EarningOpportunity("Digital Product", "Digital", "Online", "Earn ₹499", "Sell"),
        EarningOpportunity("Mini App Project", "Technology", "Remote", "Earn ₹10,000", "Apply"),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Earn Hub",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "Find opportunities, sell services, refer businesses and earn.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Text(
                "Recommended opportunities",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(opportunities) { opportunity ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        opportunity.title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(opportunity.category)
                    Text(
                        opportunity.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        opportunity.earning,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Button(
                        onClick = { onOpportunitySelected(opportunity) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(opportunity.action)
                    }
                }
            }
        }
    }
}
