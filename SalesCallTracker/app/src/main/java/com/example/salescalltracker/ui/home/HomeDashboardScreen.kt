package com.example.salescalltracker.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
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
        Text("Today's Calls: ${if (state is HomeDashboardUiState.Success) (state as HomeDashboardUiState.Success).callsToday else 0}")
        Text("People: ${if (state is HomeDashboardUiState.Success) (state as HomeDashboardUiState.Success).peopleCount else 0}")
        Text("Meetings Today: ${if (state is HomeDashboardUiState.Success) (state as HomeDashboardUiState.Success).meetingsToday else 0}")
    }
}




