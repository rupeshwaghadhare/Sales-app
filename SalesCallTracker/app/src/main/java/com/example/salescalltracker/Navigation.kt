@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.salescalltracker

import com.example.salescalltracker.ui.calls.CallLogScreen
import com.example.salescalltracker.ui.calls.CallDetailsScreen

import com.example.salescalltracker.ui.profile.ProfileScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.ui.chat.ChatListScreen
import com.example.salescalltracker.ui.chat.ChatScreen
import com.example.salescalltracker.ui.main.MainScreen
import com.example.salescalltracker.ui.people.PeopleScreen

data class NavigationCall(
  val number: String,
  val type: String,
  val date: String,
  val duration: String,
)

@Composable
fun MainNavigation(
  repository: ActivityRepository,
  calls: List<SalesCall>,
  onLoadCalls: () -> Unit,
) {
  val backStack = rememberNavBackStack(Main)

  Scaffold(
    bottomBar = {
      NavigationBar {
        NavigationBarItem(
          selected = backStack.lastOrNull() == Main,
          onClick = { backStack.replaceTop(Main) },
          icon = {},
          label = { Text("Home") },
        )
        NavigationBarItem(
          selected = backStack.lastOrNull() == Chats,
          onClick = { backStack.replaceTop(Chats) },
          icon = {},
          label = { Text("Chats") },
        )
        NavigationBarItem(
          selected = backStack.lastOrNull() == People,
          onClick = { backStack.replaceTop(People) },
          icon = {},
          label = { Text("People") },
        )
        NavigationBarItem(
          selected = backStack.lastOrNull() == Calls,
          onClick = { backStack.replaceTop(Calls) },
          icon = {},
          label = { Text("Calls") },
        )
        NavigationBarItem(
          selected = backStack.lastOrNull() == More,
          onClick = { backStack.replaceTop(More) },
          icon = {},
          label = { Text("More") },
        )
      }
    },
  ) { paddingValues ->
    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      entryProvider =
        entryProvider {
          entry<Main> {
            MainScreen(
              onItemClick = { navKey -> backStack.add(navKey) },
              repository = repository,
            )
          }
          entry<People> {
            PeopleScreen(repository = repository)
          }
          entry<Chats> {
            ChatListScreen(
              repository = repository,
              onOpenConversation = { conversationId -> backStack.add(ChatConversation(conversationId)) },
            )
          }
          entry<ChatConversation> { key ->
            ChatScreen(
              conversationId = key.conversationId,
              repository = repository,
              onBack = { backStack.removeLastOrNull() },
            )
          }
          entry<Calls> {
            CallLogScreen(
              calls = calls,
              onLoadCalls = onLoadCalls,
              onViewDetails = { number -> backStack.add(CallDetails(number)) },
            )
          }
          entry<CallDetails> { key ->
            CallDetailsScreen(
              number = key.number,
              calls = calls,
              onBack = { backStack.removeLastOrNull() },
            )
          }
          entry<More> {
            MoreScreen()
          }
        },
    )
  }
}

private fun <T> MutableList<T>.replaceTop(destination: T) {
  if (isEmpty()) add(destination) else this[lastIndex] = destination
}

@Composable
private fun MoreScreen() {
  Scaffold(
    topBar = {
      androidx.compose.material3.TopAppBar(
        title = { Text("More") }
      )
    },
  ) { paddingValues ->
    androidx.compose.foundation.lazy.LazyColumn(
      modifier = Modifier
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

      item {
        androidx.compose.material3.Card(
          modifier = Modifier.fillMaxWidth(),
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Text("👤 Profile", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Text(
              "Manage your personal and business profile",
              color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      item { Text("Earn", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("💰 Earn Hub", "Find ways to earn through sales, services and referrals")
      }

      item {
        MoreMenuItem("🎯 Opportunities", "Find jobs, leads, projects and business opportunities")
      }

      item {
        MoreMenuItem("🤝 Refer & Earn", "Refer products and services and track commissions")
      }

      item { Text("Commerce", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("🛒 Marketplace", "Buy and sell products and services")
      }

      item {
        MoreMenuItem("📦 Digital Products", "Sell PDFs, templates, courses and digital products")
      }

      item { Text("Create", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("🚀 Mini App Builder", "Create simple business apps without coding")
      }

      item {
        MoreMenuItem("🌐 Website & Store", "Create a business website or online store")
      }

      item { Text("Business", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("💼 Leads & Customers", "Manage leads, customers and sales")
      }

      item {
        MoreMenuItem("📅 Tasks & Meetings", "Manage follow-ups, tasks and meetings")
      }

      item {
        MoreMenuItem("👥 Team & Groups", "Collaborate with your team and business partners")
      }

      item { Text("Money", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("💳 Wallet & Earnings", "Track earnings, transactions and payouts")
      }

      item { Text("Settings", style = androidx.compose.material3.MaterialTheme.typography.titleMedium) }

      item {
        MoreMenuItem("⚙️ Settings", "Account, notifications, privacy, security and help")
      }
    }
  }
}

@Composable
private fun MoreMenuItem(
  title: String,
  description: String,
) {
  androidx.compose.material3.OutlinedCard(
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Text(
        title,
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
      )
      Text(
        description,
        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
