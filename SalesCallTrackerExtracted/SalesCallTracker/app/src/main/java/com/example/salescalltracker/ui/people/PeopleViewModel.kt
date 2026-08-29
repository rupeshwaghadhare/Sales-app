package com.example.salescalltracker.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PeopleViewModel(
    private val repository: ActivityRepository,
) : ViewModel() {
    private val selectedPersonId = MutableStateFlow<String?>(null)
    private val peopleFlow = repository.observePeople()
    private val activitiesFlow = repository.observeActivities()

    val uiState: StateFlow<PeopleUiState> = combine(
        peopleFlow,
        activitiesFlow,
        selectedPersonId,
    ) { people, activities, selectedId ->
        val selectedPerson = people.firstOrNull { it.id == selectedId }
        val personActivities = if (selectedPerson != null) {
            activities.filter { it.personId == selectedPerson.id }
                .sortedByDescending { it.timestamp }
        } else emptyList()

        PeopleUiState.Success(
            people = people,
            selectedPerson = selectedPerson,
            personActivities = personActivities,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PeopleUiState.Loading,
    )

    fun selectPerson(person: Person) {
        selectedPersonId.value = person.id
    }

    fun addPerson(name: String, phoneNumber: String, relationshipTypes: Set<RelationshipType>) {
        if (name.isBlank()) return
        val validTypes = relationshipTypes.ifEmpty { setOf(RelationshipType.OTHER) }
        val person = Person(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            phoneNumber = phoneNumber.takeIf { it.isNotBlank() },
            relationshipTypes = validTypes,
        )
        viewModelScope.launch {
            repository.upsertPerson(person)
            selectedPersonId.value = person.id
        }
    }

    fun addPerson(name: String, phoneNumber: String, relationshipType: RelationshipType) {
        addPerson(name, phoneNumber, setOf(relationshipType))
    }

    fun updatePerson(person: Person) {
        val normalizedPerson = person.copy(
            relationshipTypes = person.relationshipTypes.ifEmpty { setOf(RelationshipType.OTHER) },
        )
        viewModelScope.launch {
            repository.upsertPerson(normalizedPerson)
            selectedPersonId.value = normalizedPerson.id
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            repository.deletePerson(person)
            selectedPersonId.value = null
        }
    }

    fun addActivityForPerson(personId: String, type: ActivityType, title: String, description: String = "") {
        if (title.isBlank()) return
        val activity = Activity(
            id = UUID.randomUUID().toString(),
            personId = personId,
            type = type,
            title = title.trim(),
            description = description.trim(),
            timestamp = System.currentTimeMillis(),
            completed = false,
        )
        viewModelScope.launch {
            repository.upsertActivity(activity)
        }
    }

    fun updateActivity(activity: Activity) {
        viewModelScope.launch { repository.upsertActivity(activity) }
    }

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch { repository.deleteActivity(activity) }
    }
}
