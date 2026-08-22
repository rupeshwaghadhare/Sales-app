@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.salescalltracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.salescalltracker.theme.SalesCallTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SalesCall(
    val number: String,
    val type: String,
    val date: String,
    val duration: String,
    val customerName: String = "",
    val notes: String = "",
    val status: String = "New",
    val followUp: String = "",
    val automaticNote: String = automaticCallNote(type, duration)
)

enum class MainTab {
    HOME,
    CALLS,
    PROSPECTS,
    CALENDAR,
    AI
}

enum class ProspectStatus {
    HOT,
    WARM,
    NEW
}

enum class AvailabilityState {
    AVAILABLE,
    ON_CALL,
    UNKNOWN
}

data class Prospect(
    val id: String,
    val name: String,
    val phone: String,
    val status: ProspectStatus,
    val availability: AvailabilityState,
    val leadScore: Int,
    val isPriority: Boolean,
    val whatHappened: String,
    val interestLevel: String,
    val whatToDoNext: String,
    val notes: String,
    val history: List<String>
)

fun automaticCallNote(
    type: String,
    duration: String
): String {
    val durationSeconds = duration
        .removeSuffix(" sec")
        .toLongOrNull()
        ?: 0L

    return when (type) {
        "Outgoing" -> when {
            durationSeconds == 0L -> "No answer / Call not connected"
            durationSeconds <= 10L -> "Outgoing call — very short conversation"
            else -> "Outgoing call — connected"
        }

        "Incoming", "Missed" -> if (durationSeconds == 0L) {
            "Missed/incomplete incoming call"
        } else {
            "Incoming call — connected"
        }

        else -> "Call activity recorded"
    }
}

private fun isSameDay(dateText: String, reference: Date): Boolean {
    val parser = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return try {
        val parsed = parser.parse(dateText) ?: return false
        val calendar = java.util.Calendar.getInstance().apply {
            time = reference
        }
        val target = java.util.Calendar.getInstance().apply {
            time = parsed
        }
        calendar.get(java.util.Calendar.YEAR) == target.get(java.util.Calendar.YEAR) &&
            calendar.get(java.util.Calendar.DAY_OF_YEAR) == target.get(java.util.Calendar.DAY_OF_YEAR)
    } catch (_: Exception) {
        false
    }
}

private fun buildProspects(calls: List<SalesCall>): List<Prospect> {
    return calls.mapIndexed { index, call ->
        val name = call.customerName.ifBlank { call.number }
        val status = when ((index + call.number.length) % 3) {
            0 -> ProspectStatus.HOT
            1 -> ProspectStatus.WARM
            else -> ProspectStatus.NEW
        }
        val availability = when ((index + call.number.length) % 5) {
            0 -> AvailabilityState.AVAILABLE
            1 -> AvailabilityState.ON_CALL
            else -> AvailabilityState.UNKNOWN
        }
        val interest = when (status) {
            ProspectStatus.HOT -> "Very interested"
            ProspectStatus.WARM -> "Some interest"
            ProspectStatus.NEW -> "New contact"
        }
        Prospect(
            id = "prospect-${index}-${call.number}",
            name = name,
            phone = call.number,
            status = status,
            availability = availability,
            leadScore = 42 + ((index * 13) % 58),
            isPriority = status == ProspectStatus.HOT || ((index + 1) % 4 == 0),
            whatHappened = call.automaticNote.ifBlank { "Called and left a clear note about next steps." },
            interestLevel = interest,
            whatToDoNext = call.followUp.ifBlank { "Send a short follow-up and confirm the next step." },
            notes = call.notes.ifBlank { "No extra notes yet." },
            history = listOf(
                "${call.type} on ${call.date}",
                call.automaticNote,
                if (call.followUp.isNotBlank()) "Follow-up: ${call.followUp}" else "No follow-up set yet"
            ).filter { it.isNotBlank() }
        )
    }.filter { it.name.isNotBlank() }
}

private fun buildHomeSections(calls: List<SalesCall>): HomeSections {
    val today = Date()
    val todaysCalls = calls.filter { isSameDay(it.date, today) }
    val followUps = calls.filter { it.followUp.isNotBlank() && isSameDay(it.date, today) }
    val meetings = followUps.filter { it.followUp.contains("am", ignoreCase = true) || it.followUp.contains("pm", ignoreCase = true) || it.followUp.contains("meeting", ignoreCase = true) }
    val prospects = buildProspects(calls)
    return HomeSections(
        followUps = followUps,
        meetings = meetings,
        todaysCalls = todaysCalls,
        prospects = prospects,
        priorityProspects = prospects.filter { it.isPriority }
    )
}

private data class HomeSections(
    val followUps: List<SalesCall>,
    val meetings: List<SalesCall>,
    val todaysCalls: List<SalesCall>,
    val prospects: List<Prospect>,
    val priorityProspects: List<Prospect>
)

class MainActivity : ComponentActivity() {

    private val callLogPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                loadCallLogs()
            }
        }

    private var calls by mutableStateOf<List<SalesCall>>(emptyList())

    private val preferences by lazy {
        getSharedPreferences(
            "sales_call_tracker",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SalesCallTrackerTheme {
                CallLogScreen(
                    calls = calls,
                    onLoadCalls = { requestCallLogPermission() },
                    onSaveCall = { updatedCall -> saveCallDetails(updatedCall) }
                )
            }
        }

        requestCallLogPermission()
    }

    private fun requestCallLogPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadCallLogs()
        } else {
            callLogPermission.launch(Manifest.permission.READ_CALL_LOG)
        }
    }

    private fun loadCallLogs() {
        val result = mutableListOf<SalesCall>()
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
            ),
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext() && result.size < 100) {
                val number = it.getString(numberIndex) ?: "Unknown"
                val typeValue = it.getInt(typeIndex)
                val type = when (typeValue) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    CallLog.Calls.REJECTED_TYPE -> "Rejected"
                    else -> "Other"
                }
                val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                    .format(Date(it.getLong(dateIndex)))
                val duration = "${it.getLong(durationIndex)} sec"
                val customerName = preferences.getString("name_$number", "") ?: ""
                val notes = preferences.getString("notes_$number", "") ?: ""
                val status = preferences.getString("status_$number", "New") ?: "New"
                val followUp = preferences.getString("followup_$number", "") ?: ""

                result.add(
                    SalesCall(
                        number = number,
                        type = type,
                        date = date,
                        duration = duration,
                        customerName = customerName,
                        notes = notes,
                        status = status,
                        followUp = followUp
                    )
                )
            }
        }

        calls = result
    }

    private fun saveCallDetails(call: SalesCall) {
        preferences.edit()
            .putString("name_${call.number}", call.customerName)
            .putString("notes_${call.number}", call.notes)
            .putString("status_${call.number}", call.status)
            .putString("followup_${call.number}", call.followUp)
            .apply()
        loadCallLogs()
    }
}

@Composable
fun CallLogScreen(
    calls: List<SalesCall>,
    onLoadCalls: () -> Unit,
    onSaveCall: (SalesCall) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    var selectedCall by remember { mutableStateOf<SalesCall?>(null) }
    val homeSections = remember(calls) { buildHomeSections(calls) }
    val prospects = remember(calls) { buildProspects(calls) }
    val today = Date()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sales") }
            )
        },
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(text = tabName(tab)) },
                        alwaysShowLabel = true,
                        icon = { }
                    )
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                MainTab.HOME -> HomeTabContent(
                    sections = homeSections,
                    prospects = prospects,
                    onProspectClick = { },
                    onRefresh = onLoadCalls
                )
                MainTab.CALLS -> CallsTabContent(
                    calls = calls,
                    onRefresh = onLoadCalls,
                    onEdit = { selectedCall = it }
                )
                MainTab.PROSPECTS -> ProspectsTabContent(
                    prospects = prospects,
                    onCall = { },
                    onMessage = { }
                )
                MainTab.CALENDAR -> CalendarTabContent(
                    calls = calls,
                    today = today
                )
                MainTab.AI -> AiTabContent(
                    calls = calls,
                    prospects = prospects
                )
            }
        }
    }

    selectedCall?.let { call ->
        EditCallDialog(
            call = call,
            onDismiss = { selectedCall = null },
            onSave = { updatedCall ->
                onSaveCall(updatedCall)
                selectedCall = null
            }
        )
    }
}

private fun tabName(tab: MainTab): String = when (tab) {
    MainTab.HOME -> "Home"
    MainTab.CALLS -> "Calls"
    MainTab.PROSPECTS -> "Prospects"
    MainTab.CALENDAR -> "Calendar"
    MainTab.AI -> "AI"
}

@Composable
private fun HomeTabContent(
    sections: HomeSections,
    prospects: List<Prospect>,
    onProspectClick: () -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Keep it simple and focused",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(onClick = onRefresh) { Text("Refresh") }
            }
        }

        item {
            SummaryRow(
                sections = sections,
                prospects = prospects
            )
        }

        item {
            SectionTitle("Follow-ups")
        }
        if (sections.followUps.isEmpty()) {
            item { EmptyState("No follow-ups today") }
        } else {
            items(sections.followUps, key = { it.number + it.date + it.type }) { call ->
                CompactCallCard(call = call, onEdit = {})
            }
        }

        item {
            SectionTitle("Meetings")
        }
        if (sections.meetings.isEmpty()) {
            item { EmptyState("No meetings set") }
        } else {
            items(sections.meetings, key = { it.number + it.date + it.type }) { call ->
                CompactCallCard(call = call, onEdit = {})
            }
        }

        item {
            SectionTitle("Calls")
        }
        if (sections.todaysCalls.isEmpty()) {
            item { EmptyState("No calls yet today") }
        } else {
            items(sections.todaysCalls, key = { it.number + it.date + it.type }) { call ->
                CompactCallCard(call = call, onEdit = {})
            }
        }

        item {
            SectionTitle("Prospects")
        }
        if (sections.prospects.isEmpty()) {
            item { EmptyState("No active prospects") }
        } else {
            items(sections.prospects.take(3), key = { it.id }) { prospect ->
                ProspectRow(prospect = prospect, onCall = {}, onMessage = {})
            }
        }

        item {
            SectionTitle("Priority prospects")
        }
        if (sections.priorityProspects.isEmpty()) {
            item { EmptyState("No priority prospects today") }
        } else {
            items(sections.priorityProspects.take(3), key = { it.id }) { prospect ->
                ProspectRow(prospect = prospect, onCall = {}, onMessage = {})
            }
        }
    }
}

@Composable
private fun SummaryRow(
    sections: HomeSections,
    prospects: List<Prospect>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryChip(label = "Follow-ups", count = sections.followUps.size, modifier = Modifier.weight(1f))
        SummaryChip(label = "Meetings", count = sections.meetings.size, modifier = Modifier.weight(1f))
        SummaryChip(label = "Calls", count = sections.todaysCalls.size, modifier = Modifier.weight(1f))
        SummaryChip(label = "Prospects", count = prospects.size, modifier = Modifier.weight(1f))
        SummaryChip(label = "Priority", count = sections.priorityProspects.size, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SummaryChip(label: String, count: Int, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$count", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun EmptyState(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CompactCallCard(call: SalesCall, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = call.customerName.ifBlank { call.number },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SmallLabel(call.status)
                }
                Text(
                    text = "${call.type} • ${call.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (call.followUp.isNotBlank()) {
                    Text(
                        text = call.followUp,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = call.duration, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = call.type,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SmallLabel(text: String) {
    val color = when (text.lowercase()) {
        "hot" -> MaterialTheme.colorScheme.errorContainer
        "warm" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = when (text.lowercase()) {
        "hot" -> MaterialTheme.colorScheme.onErrorContainer
        "warm" -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun CallsTabContent(
    calls: List<SalesCall>,
    onRefresh: () -> Unit,
    onEdit: (SalesCall) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Calls", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Button(onClick = onRefresh) { Text("Refresh") }
            }
        }
        item {
            Text("${calls.size} total calls", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(calls, key = { it.number + it.date + it.type }) { call ->
            CompactCallCard(call = call, onEdit = { onEdit(call) })
        }
    }
}

@Composable
private fun ProspectsTabContent(
    prospects: List<Prospect>,
    onCall: (String) -> Unit,
    onMessage: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Prospects", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        }
        items(prospects, key = { it.id }) { prospect ->
            ProspectCard(prospect = prospect, onCall = { onCall(prospect.phone) }, onMessage = { onMessage(prospect.phone) })
        }
    }
}

@Composable
private fun ProspectCard(
    prospect: Prospect,
    onCall: () -> Unit,
    onMessage: () -> Unit
) {
    var expanded by rememberSaveable(prospect.id) { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(prospect.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(prospect.phone, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SmallStatusChip(prospect.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvailabilityBadge(prospect.availability)
                Text("Lead score ${prospect.leadScore}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                DetailSection(title = "What happened?", value = prospect.whatHappened)
                DetailSection(title = "Interest level", value = prospect.interestLevel)
                DetailSection(title = "What to do next", value = prospect.whatToDoNext)
                DetailSection(title = "Notes", value = prospect.notes)
                DetailSection(title = "Conversation history", value = prospect.history.joinToString(" • "))

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${prospect.phone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Call")
                    }
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${prospect.phone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Message")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProspectRow(
    prospect: Prospect,
    onCall: () -> Unit,
    onMessage: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(prospect.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    SmallStatusChip(prospect.status)
                }
                Text(
                    text = prospect.whatToDoNext,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                AvailabilityBadge(prospect.availability)
                Text("${prospect.leadScore}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun SmallStatusChip(status: ProspectStatus) {
    val label = when (status) {
        ProspectStatus.HOT -> "Hot"
        ProspectStatus.WARM -> "Warm"
        ProspectStatus.NEW -> "New"
    }
    val background = when (status) {
        ProspectStatus.HOT -> MaterialTheme.colorScheme.errorContainer
        ProspectStatus.WARM -> MaterialTheme.colorScheme.primaryContainer
        ProspectStatus.NEW -> MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = when (status) {
        ProspectStatus.HOT -> MaterialTheme.colorScheme.onErrorContainer
        ProspectStatus.WARM -> MaterialTheme.colorScheme.onPrimaryContainer
        ProspectStatus.NEW -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    Surface(shape = RoundedCornerShape(999.dp), color = background) {
        Text(text = label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = textColor, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun AvailabilityBadge(state: AvailabilityState) {
    val label = when (state) {
        AvailabilityState.AVAILABLE -> "Available"
        AvailabilityState.ON_CALL -> "On a call"
        AvailabilityState.UNKNOWN -> "Unknown"
    }
    val background = when (state) {
        AvailabilityState.AVAILABLE -> MaterialTheme.colorScheme.primaryContainer
        AvailabilityState.ON_CALL -> MaterialTheme.colorScheme.tertiaryContainer
        AvailabilityState.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (state) {
        AvailabilityState.AVAILABLE -> MaterialTheme.colorScheme.onPrimaryContainer
        AvailabilityState.ON_CALL -> MaterialTheme.colorScheme.onTertiaryContainer
        AvailabilityState.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(shape = RoundedCornerShape(999.dp), color = background) {
        Text(text = label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = textColor, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun DetailSection(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CalendarTabContent(calls: List<SalesCall>, today: Date) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Calendar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        }
        val upcoming = calls.filter { it.followUp.isNotBlank() }
        if (upcoming.isEmpty()) {
            item { EmptyState("No meetings or reminders yet") }
        } else {
            items(upcoming.take(10), key = { "calendar-${it.number}-${it.date}" }) { call ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = call.customerName.ifBlank { call.number }, style = MaterialTheme.typography.titleSmall)
                        Text(text = call.followUp.ifBlank { "No follow-up set" }, style = MaterialTheme.typography.bodyMedium)
                        Text(text = call.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun AiTabContent(calls: List<SalesCall>, prospects: List<Prospect>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("AI assistant", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Next best action", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (prospects.isNotEmpty()) {
                            "Follow up with ${prospects.first().name} and confirm the next step."
                        } else {
                            "Review the newest call and note the next step."
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        item {
            Text("Quick summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Calls today: ${calls.count { isSameDay(it.date, Date()) }}")
                    Text(text = "Follow-ups today: ${calls.count { it.followUp.isNotBlank() && isSameDay(it.date, Date()) }}")
                    Text(text = "Hot prospects: ${prospects.count { it.status == ProspectStatus.HOT }}")
                }
            }
        }
    }
}

@Composable
fun EditCallDialog(
    call: SalesCall,
    onDismiss: () -> Unit,
    onSave: (SalesCall) -> Unit
) {
    var customerName by remember { mutableStateOf(call.customerName) }
    var notes by remember { mutableStateOf(call.notes) }
    var status by remember { mutableStateOf(call.status) }
    var followUp by remember { mutableStateOf(call.followUp) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statuses = listOf("New", "Contacted", "Interested", "Follow-up", "Converted", "Not Interested")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Sales details") },
        text = {
            Column {
                Text(text = call.number, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        TextButton(onClick = { statusExpanded = !statusExpanded }) {
                            Text("▼")
                        }
                    }
                )
                DropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statuses.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                status = item
                                statusExpanded = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = followUp,
                    onValueChange = { followUp = it },
                    label = { Text("Follow-up") },
                    placeholder = { Text("Example: 25 Aug 2026, 10:00 AM") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Call notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    call.copy(
                        customerName = customerName.trim(),
                        notes = notes.trim(),
                        status = status,
                        followUp = followUp.trim()
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}