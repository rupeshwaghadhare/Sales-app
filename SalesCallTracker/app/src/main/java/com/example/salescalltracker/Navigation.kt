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
import com.example.salescalltracker.ui.earn.EarnHubScreen
import com.example.salescalltracker.ui.people.PeopleScreen
import com.example.salescalltracker.ui.discover.DiscoverScreen
import com.example.salescalltracker.ui.create.CreateScreen
import com.example.salescalltracker.ui.events.EventSearchScreen
import com.example.salescalltracker.ui.locations.LocationSearchScreen
import com.example.salescalltracker.ui.create.OfferingCreateScreen
import com.example.salescalltracker.ui.platform.*
import com.example.salescalltracker.ui.connect.ConnectScreen

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
          icon = { Text("🏠") },
          label = { Text("Home") },
        )

        NavigationBarItem(
          selected = backStack.lastOrNull() == Discover,
          onClick = { backStack.replaceTop(Discover) },
          icon = { Text("🔎") },
          label = { Text("Discover") },
        )

        NavigationBarItem(
          selected = backStack.lastOrNull() == Create,
          onClick = { backStack.replaceTop(Create) },
          icon = { Text("＋") },
          label = { Text("Create") },
        )

        NavigationBarItem(
          selected = backStack.lastOrNull() == Connect,
          onClick = { backStack.replaceTop(Connect) },
          icon = { Text("💬") },
          label = { Text("Connect") },
        )

        NavigationBarItem(
          selected = backStack.lastOrNull() == Profile,
          onClick = { backStack.replaceTop(Profile) },
          icon = { Text("👤") },
          label = { Text("Profile") },
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
          entry<Discover> {
            DiscoverScreen(
              onMarketplaceClick = { backStack.add(Marketplace) },
              onCampaignsClick = { backStack.add(Campaigns) },
            )
          }

          entry<Create> {
            CreateScreen(
              onBusinessClick = { backStack.add(BusinessProfile) },
              onOfferingClick = { type ->
                backStack.add(CreateOffering(type))
              },
            )
          }

          entry<Connect> {
            ConnectScreen(
              onChatsClick = { backStack.add(Chats) },
              onPeopleClick = { backStack.add(People) },
              onCallsClick = { backStack.add(Calls) },
            )
          }

          entry<Profile> {
            ProfileScreen()
          }
          entry<CreateOffering> { key ->
            OfferingCreateScreen(
              type = key.type,
              onBack = { backStack.removeLastOrNull() },
            )
          }
          entry<EventSearch> {
            EventSearchScreen(
              city = "Pune",
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Locations> {
            LocationSearchScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }
          entry<BusinessProfile> {
            BusinessProfileScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Marketplace> {
            MarketplaceScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Campaigns> {
            CampaignsScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Services> {
            ServicesScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Products> {
            ProductsScreen(
              onBack = { backStack.removeLastOrNull() },
            )
          }

          entry<Website> {
            WebsiteScreen(
              onBack = { backStack.removeLastOrNull() },
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
          entry<Earn> {
            EarnHubScreen()
          }
          entry<More> {
            MoreScreen(onEarnClick = { backStack.add(Earn) })
          }
        },
    )
  }
}

private fun <T> MutableList<T>.replaceTop(destination: T) {
  if (isEmpty()) add(destination) else this[lastIndex] = destination
}

@Composable
private fun MoreScreen(onEarnClick: () -> Unit = {}) {
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
        MoreMenuItem("💰 Earn Hub", "Find ways to earn through sales, services and referrals", onClick = onEarnClick)
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
  onClick: () -> Unit = {},
) {
  androidx.compose.material3.OutlinedCard(
    onClick = onClick,
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









