package com.example.salescalltracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.Activity
import com.example.salescalltracker.model.ActivityType
import com.example.salescalltracker.model.Person
import com.example.salescalltracker.model.RelationshipType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class HomeDashboardViewModel(
    repository: ActivityRepository,
) : ViewModel() {
    private val peopleFlow = repository.observePeople()
    private val activitiesFlow = repository.observeActivities()

    val uiState: StateFlow<HomeDashboardUiState> = combine(
        peopleFlow,
        activitiesFlow,
    ) { people, activities ->
        val today = Calendar.getInstance()
        val startOfToday = today.startOfDay()
        val startOfTomorrow = startOfToday + DAY_MILLIS
        val peopleById = people.associateBy(Person::id)
        val followUps = activities
            .filter { it.type == ActivityType.FOLLOW_UP && it.followUpDate != null }
            .sortedBy { it.followUpDate }
            .map { activity ->
                DashboardFollowUp(
                    activity = activity,
                    personName = activity.personId?.let(peopleById::get)?.name ?: "Unknown person",
                )
            }

        HomeDashboardUiState.Success(
            peopleCount = people.size,
            customerCount = people.count { RelationshipType.CUSTOMER in it.relationshipTypes },
            prospectCount = people.count { RelationshipType.PROSPECT in it.relationshipTypes },
            businessPartnerCount = people.count { RelationshipType.BUSINESS_PARTNER in it.relationshipTypes },
            callsToday = activities.count { it.type == ActivityType.CALL && it.timestamp in startOfToday until startOfTomorrow },
            meetingsToday = activities.count { it.type == ActivityType.MEETING && it.timestamp in startOfToday until startOfTomorrow },
            pendingTasks = activities.count { it.type == ActivityType.TASK && !it.completed },
            followUpsToday = followUps.count { it.activity.followUpDate in startOfToday until startOfTomorrow },
            todaysFollowUps = followUps.filter { it.activity.followUpDate in startOfToday until startOfTomorrow },
            overdueFollowUps = followUps.filter { it.activity.followUpDate!! < startOfToday && !it.activity.completed },
            upcomingFollowUps = followUps.filter { it.activity.followUpDate!! >= startOfTomorrow && !it.activity.completed },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeDashboardUiState.Loading,
    )

    private companion object {
        const val DAY_MILLIS = 24 * 60 * 60 * 1000L
    }
}

sealed interface HomeDashboardUiState {
    data object Loading : HomeDashboardUiState
    data class Error(val message: String) : HomeDashboardUiState
    data class Success(
        val peopleCount: Int,
        val customerCount: Int,
        val prospectCount: Int,
        val businessPartnerCount: Int,
        val callsToday: Int,
        val meetingsToday: Int,
        val pendingTasks: Int,
        val followUpsToday: Int,
        val todaysFollowUps: List<DashboardFollowUp>,
        val overdueFollowUps: List<DashboardFollowUp>,
        val upcomingFollowUps: List<DashboardFollowUp>,
    ) : HomeDashboardUiState
}

data class DashboardFollowUp(
    val activity: Activity,
    val personName: String,
)

private fun Calendar.startOfDay(): Long = apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis
