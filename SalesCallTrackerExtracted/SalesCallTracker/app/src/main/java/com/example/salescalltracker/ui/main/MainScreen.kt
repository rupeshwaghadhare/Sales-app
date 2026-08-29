package com.example.salescalltracker.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.salescalltracker.Chats
import com.example.salescalltracker.Calls
import com.example.salescalltracker.People
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.theme.SalesCallTrackerTheme
import com.example.salescalltracker.ui.home.DashboardFollowUp
import com.example.salescalltracker.ui.home.HomeDashboardUiState
import com.example.salescalltracker.ui.home.HomeDashboardViewModel

@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit,
  repository: ActivityRepository,
  modifier: Modifier = Modifier,
  viewModel: HomeDashboardViewModel = viewModel { HomeDashboardViewModel(repository) },
) {
  val state = viewModel.uiState.collectAsStateWithLifecycle().value
  when (state) {
    HomeDashboardUiState.Loading -> {
      Text("Loading dashboard...", modifier = modifier)
    }
    is HomeDashboardUiState.Success -> {
      val successState = state
      HomeDashboard(state = successState, onItemClick = onItemClick, modifier = modifier)
    }
    is HomeDashboardUiState.Error -> {
      val errorState = state
      Text(errorState.message, modifier = modifier)
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeDashboard(
  state: HomeDashboardUiState.Success,
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Good morning", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Sales overview", style = MaterialTheme.typography.headlineMedium)
        Text("Your business activity at a glance", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    item { SummaryGrid(state) }
    item {
      Text("Quick actions", style = MaterialTheme.typography.titleLarge)
      FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onItemClick(People) }) { Text("People") }
        Button(onClick = { onItemClick(Calls) }) { Text("Call log") }
        Button(onClick = { onItemClick(Chats) }) { Text("New chat") }
      }
    }
    item { Text("Today's follow-ups", style = MaterialTheme.typography.titleLarge) }
    if (state.todaysFollowUps.isEmpty()) {
      item { EmptyDashboardState("No follow-ups today.") }
    } else {
      items(state.todaysFollowUps) { FollowUpCard(it) }
    }
    item { Text("Overdue", style = MaterialTheme.typography.titleLarge) }
    if (state.overdueFollowUps.isEmpty()) {
      item { EmptyDashboardState("No overdue follow-ups.") }
    } else {
      items(state.overdueFollowUps) { FollowUpCard(it) }
    }
    item { Text("Upcoming", style = MaterialTheme.typography.titleLarge) }
    if (state.upcomingFollowUps.isEmpty()) {
      item { EmptyDashboardState("No upcoming follow-ups.") }
    } else {
      items(state.upcomingFollowUps.take(5)) { FollowUpCard(it) }
    }
  }
}

@Composable
private fun SummaryGrid(state: HomeDashboardUiState.Success) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      SummaryCard("People", state.peopleCount, Modifier.weight(1f))
      SummaryCard("Customers", state.customerCount, Modifier.weight(1f))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      SummaryCard("Prospects", state.prospectCount, Modifier.weight(1f))
      SummaryCard("Partners", state.businessPartnerCount, Modifier.weight(1f))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      SummaryCard("Calls today", state.callsToday, Modifier.weight(1f))
      SummaryCard("Meetings", state.meetingsToday, Modifier.weight(1f))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      SummaryCard("Pending tasks", state.pendingTasks, Modifier.weight(1f))
      SummaryCard("Follow-ups today", state.followUpsToday, Modifier.weight(1f))
    }
  }
}

@Composable
private fun SummaryCard(label: String, value: Int, modifier: Modifier) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Column(Modifier.padding(12.dp)) {
      Text(value.toString(), style = MaterialTheme.typography.headlineMedium)
      Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun FollowUpCard(followUp: DashboardFollowUp) {
  Card(
    Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Column(Modifier.padding(12.dp)) {
      Text(followUp.personName, style = MaterialTheme.typography.titleMedium)
      Text(followUp.activity.title)
      Text(if (followUp.activity.completed) "Completed" else "Open", color = MaterialTheme.colorScheme.primary)
    }
  }
}

@Composable
private fun EmptyDashboardState(message: String) {
  Surface(Modifier.fillMaxWidth(), tonalElevation = 1.dp) {
    Text(message, Modifier.padding(16.dp))
  }
}

@Composable
internal fun MainScreen(data: List<String>, modifier: Modifier = Modifier) {
  Column(modifier) { data.forEach { Greeting(it) } }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  SalesCallTrackerTheme { MainScreen(listOf("Android")) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
fun MainScreenPortraitPreview() {
  SalesCallTrackerTheme { MainScreen(listOf("Android")) }
}
