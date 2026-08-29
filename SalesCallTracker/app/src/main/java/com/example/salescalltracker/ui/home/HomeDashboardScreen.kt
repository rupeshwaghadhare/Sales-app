package com.example.salescalltracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeDashboardScreen(
    viewModel: HomeDashboardViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sales Call Tracker")
        Text("Home Dashboard")

        when (val currentState = state) {
            HomeDashboardUiState.Loading -> {
                Text("Loading dashboard...")
            }

            is HomeDashboardUiState.Error -> {
                Text("Error: ${currentState.message}")
            }

            is HomeDashboardUiState.Success -> {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("People: ${currentState.peopleCount}")
                        Text("Customers: ${currentState.customerCount}")
                        Text("Prospects: ${currentState.prospectCount}")
                        Text("Business Partners: ${currentState.businessPartnerCount}")
                    }
                }

                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Calls Today: ${currentState.callsToday}")
                        Text("Meetings Today: ${currentState.meetingsToday}")
                        Text("Pending Tasks: ${currentState.pendingTasks}")
                        Text("Follow-ups Today: ${currentState.followUpsToday}")
                    }
                }
            }
        }
    }
}
