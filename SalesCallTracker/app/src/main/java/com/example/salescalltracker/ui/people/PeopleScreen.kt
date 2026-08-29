@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.example.salescalltracker.ui.people

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import java.text.DateFormat
import java.util.Date


@Composable
fun PeopleScreen(
    repository: ActivityRepository,
    viewModel: PeopleViewModel = viewModel { PeopleViewModel(repository) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val current = state) {
        PeopleUiState.Loading -> Text("Loading people...")
        is PeopleUiState.Error -> Text(current.message)
        is PeopleUiState.Success -> PeopleWorkspace(current, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PeopleWorkspace(state: PeopleUiState.Success, viewModel: PeopleViewModel) {
    var selectedPerson by remember { mutableStateOf<Person?>(null) }
    var editingPerson by remember { mutableStateOf<Person?>(null) }
    var showForm by remember { mutableStateOf(false) }
    var showNote by remember { mutableStateOf<Person?>(null) }
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf<RelationshipType?>(null) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.selectedPerson) {
        if (selectedPerson != null) selectedPerson = state.selectedPerson
    }

    val filteredPeople = state.people.filter { person ->
        val matchesQuery = query.isBlank() || person.name.contains(query, true) ||
            person.phoneNumber.orEmpty().contains(query, true)
        val matchesFilter = filter == null || filter in person.relationshipTypes
        matchesQuery && matchesFilter
    }

    if (selectedPerson != null) {
        PersonDetailsScreen(
            person = selectedPerson!!,
            activities = if (state.selectedPerson?.id == selectedPerson?.id) state.personActivities else emptyList(),
            onBack = { selectedPerson = null },
            onEdit = { editingPerson = selectedPerson },
            onAddNote = { showNote = selectedPerson },
            onDelete = {
                viewModel.deletePerson(selectedPerson!!)
                selectedPerson = null
            },
            onCall = { dial(context, selectedPerson!!.phoneNumber) },
            onWhatsApp = { openWhatsApp(context, selectedPerson!!.phoneNumber) },
            onDeleteActivity = viewModel::deleteActivity,
        )
    } else {
        Scaffold(
            topBar = { TopAppBar(title = { Text("People") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { showForm = true }) { Text("+") }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search people...") },
                        singleLine = true,
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = filter == null, onClick = { filter = null }, label = { Text("All") })
                        listOf(RelationshipType.CUSTOMER, RelationshipType.PROSPECT, RelationshipType.BUSINESS_PARTNER, RelationshipType.FRIEND)
                            .forEach { type ->
                                FilterChip(selected = filter == type, onClick = { filter = type }, label = { Text(type.label()) })
                            }
                    }
                }
                if (filteredPeople.isEmpty()) {
                    item {
                        Text("No people yet\nAdd your first business relationship.", modifier = Modifier.padding(vertical = 32.dp))
                    }
                } else {
                    items(filteredPeople, key = { it.id }) { person ->
                        PersonCard(
                            person = person,
                            activities = state.personActivities.takeIf { state.selectedPerson?.id == person.id }.orEmpty(),
                            onOpen = {
                                viewModel.selectPerson(person)
                                selectedPerson = person
                            },
                            onEdit = { editingPerson = person },
                            onNote = { showNote = person },
                            onCall = { dial(context, person.phoneNumber) },
                            onWhatsApp = { openWhatsApp(context, person.phoneNumber) },
                            onDelete = { viewModel.deletePerson(person) },
                        )
                    }
                }
            }
        }
    }

    if (showForm) {
        PersonFormDialog(null, viewModel::addPerson, { showForm = false })
    }
    editingPerson?.let { person ->
        PersonFormDialog(
            person,
            { name, phone, types ->
                viewModel.updatePerson(
                    person.copy(
                        name = name,
                        phoneNumber = phone.takeIf { it.isNotBlank() },
                        relationshipTypes = types,
                    ),
                )
                editingPerson = null
            },
            { editingPerson = null },
        )
    }
    showNote?.let { person ->
        NoteDialog(
            onSave = { title, description -> viewModel.addActivityForPerson(person.id, ActivityType.NOTE, title, description); showNote = null },
            onDismiss = { showNote = null },
        )
    }
}

@Composable
private fun PersonCard(
    person: Person,
    activities: List<Activity>,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onNote: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onOpen) { Text(person.name, style = MaterialTheme.typography.titleMedium) }
                Column {
                    IconButton(onClick = { menuOpen = true }) { Text("More") }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, onClick = { menuOpen = false; onEdit() })
                        DropdownMenuItem(text = { Text("Add Note") }, onClick = { menuOpen = false; onNote() })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = { menuOpen = false; onDelete() })
                    }
                }
            }
            RelationshipChips(person.relationshipTypes)
            Text(person.phoneNumber ?: "No phone number")
            Text("Activities: ${activities.size}", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onCall) { Text("Call") }
                TextButton(onClick = onWhatsApp) { Text("WhatsApp") }
                TextButton(onClick = onNote) { Text("Note") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonDetailsScreen(
    person: Person,
    activities: List<Activity>,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddNote: () -> Unit,
    onDelete: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onDeleteActivity: (Activity) -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(person.name) },
            navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            actions = {
                IconButton(onClick = { menuOpen = true }) { Text("More") }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(text = { Text("Edit Person") }, onClick = { menuOpen = false; onEdit() })
                    DropdownMenuItem(text = { Text("Delete Person") }, onClick = { menuOpen = false; onDelete() })
                }
            },
        )
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text(person.phoneNumber ?: "No phone number")
                RelationshipChips(person.relationshipTypes)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(onClick = onCall) { Text("Call") }
                    Button(onClick = onWhatsApp) { Text("WhatsApp") }
                    TextButton(onClick = onAddNote) { Text("Note") }
                }
                Text("Activity Timeline", style = MaterialTheme.typography.titleLarge)
            }
            if (activities.isEmpty()) item { Text("No interactions yet") }
            items(activities, key = { it.id }) { activity ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(activity.type.name, style = MaterialTheme.typography.labelLarge)
                        Text(activity.title, style = MaterialTheme.typography.titleMedium)
                        if (activity.description.isNotBlank()) Text(activity.description)
                        Text(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(activity.timestamp)))
                        TextButton(onClick = { onDeleteActivity(activity) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonFormDialog(person: Person?, onSave: (String, String, Set<RelationshipType>) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(person?.name.orEmpty()) }
    var phone by remember { mutableStateOf(person?.phoneNumber.orEmpty()) }
    var types by remember { mutableStateOf(person?.relationshipTypes.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (person == null) "Add Person" else "Edit Person") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(phone, { phone = it }, label = { Text("Phone number") }, singleLine = true)
                Text("Relationship types")
                RelationshipChips(RelationshipType.entries, types) { type -> types = if (type in types) types - type else types + type }
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank()) onSave(name, phone, types) }) { Text(if (person == null) "Save Person" else "Save Changes") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun NoteDialog(onSave: (String, String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") })
                OutlinedTextField(description, { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = { TextButton(onClick = { if (title.isNotBlank()) onSave(title, description) }) { Text("Save Note") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun RelationshipChips(types: Set<RelationshipType>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) { types.forEach { FilterChip(selected = true, onClick = {}, label = { Text(it.label()) }) } }
}

@Composable
private fun RelationshipChips(all: List<RelationshipType>, selected: Set<RelationshipType>, onClick: (RelationshipType) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) { all.forEach { FilterChip(selected = it in selected, onClick = { onClick(it) }, label = { Text(it.label()) }) } }
}

private fun RelationshipType.label(): String = name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }

private fun dial(context: Context, phone: String?) {
    phone?.takeIf { it.isNotBlank() }?.let { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(it)}"))) }
}

private fun openWhatsApp(context: Context, phone: String?) {
    phone?.filter(Char::isDigit)?.takeIf { it.isNotBlank() }?.let {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$it")))
        } catch (_: ActivityNotFoundException) {
        }
    }
}
