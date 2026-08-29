@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.salescalltracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
            CallLogScreen(calls = calls, onLoadCalls = onLoadCalls)
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
    topBar = { androidx.compose.material3.TopAppBar(title = { Text("More") }) },
  ) { paddingValues ->
    Column(
      modifier = Modifier.padding(paddingValues).padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text("Business tools")
      Text("More CRM tools will appear here as they become available.")
    }
  }
}
