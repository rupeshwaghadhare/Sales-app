package com.example.salescalltracker.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class IdentityOption(
    val id: String,
    val title: String,
)

data class GoalOption(
    val id: String,
    val title: String,
)

private val identityOptions = listOf(
    IdentityOption("STUDENT", "🎓 Student"),
    IdentityOption("CREATOR", "🎨 Creator"),
    IdentityOption("FREELANCER", "💻 Freelancer"),
    IdentityOption("BUSINESS_OWNER", "🏪 Business Owner"),
    IdentityOption("SELLER", "🛍️ Seller"),
    IdentityOption("PROFESSIONAL", "👨‍💼 Professional"),
    IdentityOption("MARKETER", "📣 Marketer"),
    IdentityOption("REFERRER", "🤝 Referrer"),
    IdentityOption("EXPLORER", "👤 Just Exploring"),
)

private val goalOptions = listOf(
    GoalOption("LEARN", "📚 Learn"),
    GoalOption("FIND_WORK", "💼 Find Work"),
    GoalOption("SELL", "🛒 Sell"),
    GoalOption("PROMOTE", "📣 Promote"),
    GoalOption("CREATE_CONTENT", "🎨 Create Content"),
    GoalOption("FIND_CUSTOMERS", "🎯 Find Customers"),
    GoalOption("COLLABORATE", "🤝 Collaborate"),
    GoalOption("REFER", "🔗 Refer"),
    GoalOption("EARN", "💰 Earn"),
    GoalOption("BUILD_BUSINESS", "🚀 Build a Business"),
)

@Composable
fun DefineYourselfScreen(
    onComplete: (roles: Set<String>, goals: Set<String>) -> Unit,
) {
    var selectedRoles by remember { mutableStateOf(emptySet<String>()) }
    var selectedGoals by remember { mutableStateOf(emptySet<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    "Define Yourself",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "Tell us how you want to use the app. You can change this later.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Text(
                "Who are you?",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                identityOptions.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        row.forEach { option ->
                            FilterChip(
                                selected = option.id in selectedRoles,
                                onClick = {
                                    selectedRoles =
                                        if (option.id in selectedRoles) {
                                            selectedRoles - option.id
                                        } else {
                                            selectedRoles + option.id
                                        }
                                },
                                label = { Text(option.title) },
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                "What do you want to do?",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(goalOptions.chunked(2)) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { option ->
                    FilterChip(
                        selected = option.id in selectedGoals,
                        onClick = {
                            selectedGoals =
                                if (option.id in selectedGoals) {
                                    selectedGoals - option.id
                                } else {
                                    selectedGoals + option.id
                                }
                        },
                        label = { Text(option.title) },
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    onComplete(selectedRoles, selectedGoals)
                },
                enabled = selectedRoles.isNotEmpty() && selectedGoals.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Continue")
            }
        }
    }
}
