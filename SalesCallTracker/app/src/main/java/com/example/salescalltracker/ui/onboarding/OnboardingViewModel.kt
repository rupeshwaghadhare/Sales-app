package com.example.salescalltracker.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salescalltracker.data.preferences.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferences: UserPreferences,
) : ViewModel() {

    val onboardingCompleted: StateFlow<Boolean> =
        preferences.onboardingCompleted
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false,
            )

    fun completeOnboarding(
        roles: Set<String>,
        goals: Set<String>,
    ) {
        viewModelScope.launch {
            preferences.saveIdentity(
                roles = roles,
                goals = goals,
            )
        }
    }
}
