package com.example.salescalltracker.ui.people

import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType

sealed interface PeopleUiState {
    data object Loading : PeopleUiState
    data class Error(val message: String) : PeopleUiState
    data class Success(
        val people: List<Person> = emptyList(),
        val selectedPerson: Person? = null,
        val personActivities: List<Activity> = emptyList(),
    ) : PeopleUiState
}

data class PersonFormState(
    val name: String = "",
    val phoneNumber: String = "",
    val relationshipTypes: Set<RelationshipType> = emptySet(),
) {
    val relationshipType: RelationshipType
        get() = relationshipTypes.firstOrNull() ?: RelationshipType.OTHER
}

fun PersonFormState.isValid(): Boolean = name.isNotBlank()
