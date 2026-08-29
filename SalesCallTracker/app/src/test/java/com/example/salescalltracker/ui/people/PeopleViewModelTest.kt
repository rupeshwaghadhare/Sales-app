package com.example.salescalltracker.ui.people

import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PeopleViewModelTest {

    private fun collectUiState(scope: CoroutineScope, viewModel: PeopleViewModel): Job =
        scope.launch { viewModel.uiState.collect { } }

    @Test
    fun addPerson_generatesUniqueId() = runTest {
        val repo = FakePeopleRepository()
        val viewModel = PeopleViewModel(repo)
        val collection = collectUiState(this, viewModel)

        viewModel.addPerson("Rahul", "9876543210", setOf(RelationshipType.FRIEND, RelationshipType.COLLEAGUE))
        advanceUntilIdle()

        val success = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        val first = success.people.first()

        viewModel.addPerson("Aisha", "1234567890", setOf(RelationshipType.CLIENT, RelationshipType.CUSTOMER))
        advanceUntilIdle()

        val updatedSuccess = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        val second = updatedSuccess.people.last()

        assertEquals("Rahul", first.name)
        assertTrue(first.id.isNotBlank())
        assertEquals(setOf(RelationshipType.FRIEND, RelationshipType.COLLEAGUE), first.relationshipTypes)
        assertTrue(second.id.isNotBlank())
        assertTrue(first.id != second.id)

        collection.cancel()
    }

    @Test
    fun addPerson_updatesRepositoryAndSelection() = runTest {
        val repo = FakePeopleRepository()
        val viewModel = PeopleViewModel(repo)
        val collection = collectUiState(this, viewModel)

        viewModel.addPerson("Rahul", "9876543210", RelationshipType.CUSTOMER)
        advanceUntilIdle()

        val success = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        assertEquals(1, success.people.size)
        assertEquals("Rahul", success.people.first().name)
        assertEquals(RelationshipType.CUSTOMER, success.people.first().relationshipType)
        assertEquals(success.people.first().id, success.selectedPerson?.id)

        collection.cancel()
    }

    @Test
    fun updatePerson_preservesIdAndKeepsActivitiesConnected() = runTest {
        val repo = FakePeopleRepository()
        val viewModel = PeopleViewModel(repo)
        val collection = collectUiState(this, viewModel)

        viewModel.addPerson("Rahul", "9876543210", RelationshipType.FRIEND)
        advanceUntilIdle()

        val successBefore = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        val original = successBefore.people.first()
        val originalId = original.id

        viewModel.addActivityForPerson(originalId, ActivityType.CALL, "Follow-up")
        advanceUntilIdle()

        viewModel.updatePerson(
            original.copy(
                name = "Rahul Sharma",
                relationshipTypes = setOf(
                    RelationshipType.FRIEND,
                    RelationshipType.COLLEAGUE,
                    RelationshipType.BUSINESS_PARTNER,
                ),
            ),
        )
        advanceUntilIdle()

        val successAfter = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        val updated = successAfter.people.first()
        assertEquals(originalId, updated.id)
        assertEquals("Rahul Sharma", updated.name)
        assertEquals(
            setOf(
                RelationshipType.FRIEND,
                RelationshipType.COLLEAGUE,
                RelationshipType.BUSINESS_PARTNER,
            ),
            updated.relationshipTypes,
        )
        assertEquals(1, successAfter.personActivities.size)
        assertEquals(originalId, successAfter.personActivities.first().personId)

        collection.cancel()
    }

    @Test
    fun addActivityForPerson_keepsActivityForSelectedPerson() = runTest {
        val repo = FakePeopleRepository()
        val viewModel = PeopleViewModel(repo)
        val collection = collectUiState(this, viewModel)

        viewModel.addPerson("Priya", "123456789", RelationshipType.PROSPECT)
        advanceUntilIdle()

        val successBefore = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        val person = successBefore.people.first()

        viewModel.selectPerson(person)
        viewModel.addActivityForPerson(person.id, ActivityType.CALL, "Follow-up")
        advanceUntilIdle()

        val successAfter = viewModel.uiState
            .first { it is PeopleUiState.Success } as PeopleUiState.Success
        assertEquals(1, successAfter.personActivities.size)
        assertEquals("Follow-up", successAfter.personActivities.first().title)
        assertEquals(person.id, successAfter.personActivities.first().personId)

        collection.cancel()
    }
}

private class FakePeopleRepository : ActivityRepository {
    private val peopleFlow = MutableStateFlow<List<Person>>(emptyList())
    private val activitiesFlow = MutableStateFlow<List<Activity>>(emptyList())

    override fun observePeople(): Flow<List<Person>> = peopleFlow

    override fun observeActivities(): Flow<List<Activity>> = activitiesFlow

    override fun observeActivitiesForPerson(personId: String): Flow<List<Activity>> =
        activitiesFlow.map { activities -> activities.filter { it.personId == personId } }

    override suspend fun upsertPerson(person: Person) {
        val current = peopleFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == person.id }
        if (index >= 0) current[index] = person else current.add(person)
        peopleFlow.value = current
    }

    override suspend fun deletePerson(person: Person) {
        peopleFlow.value = peopleFlow.value.filterNot { it.id == person.id }
        activitiesFlow.value = activitiesFlow.value.filterNot { it.personId == person.id }
    }

    override suspend fun upsertActivity(activity: Activity) {
        val current = activitiesFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == activity.id }
        if (index >= 0) current[index] = activity else current.add(activity)
        activitiesFlow.value = current
    }

    override suspend fun deleteActivity(activity: Activity) {
        activitiesFlow.value = activitiesFlow.value.filterNot { it.id == activity.id }
    }

    override suspend fun getPersonById(id: String): Person? = peopleFlow.value.firstOrNull { it.id == id }

    override suspend fun getActivityById(id: String): Activity? = activitiesFlow.value.firstOrNull { it.id == id }

    override fun observeConversations(): Flow<List<com.example.salescalltracker.data.Conversation>> = kotlinx.coroutines.flow.emptyFlow()

    override fun observeMessages(conversationId: String): Flow<List<com.example.salescalltracker.data.ChatMessage>> = kotlinx.coroutines.flow.emptyFlow()

    override suspend fun getOrCreateConversation(personId: String) = error("Not used in PeopleViewModelTest")

    override suspend fun sendMessage(message: com.example.salescalltracker.data.ChatMessage) = Unit

    override suspend fun deleteMessage(message: com.example.salescalltracker.data.ChatMessage) = Unit

    override suspend fun markConversationRead(conversationId: String) = Unit

    override suspend fun markConversationUnread(conversationId: String) = Unit

    override suspend fun setConversationPinned(conversationId: String, value: Boolean) = Unit

    override suspend fun setConversationMuted(conversationId: String, value: Boolean) = Unit

    override suspend fun setConversationArchived(conversationId: String, value: Boolean) = Unit
}
