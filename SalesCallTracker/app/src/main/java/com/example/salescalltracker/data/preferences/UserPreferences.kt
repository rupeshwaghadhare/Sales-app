package com.example.salescalltracker.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferences(
    private val context: Context,
) {

    private object Keys {
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
        val roles = stringPreferencesKey("roles")
        val goals = stringPreferencesKey("goals")
        val activeWorkspace = stringPreferencesKey("active_workspace")
    }

    val onboardingCompleted: Flow<Boolean> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[Keys.onboardingCompleted] ?: false
        }

    val roles: Flow<Set<String>> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[Keys.roles]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    val goals: Flow<Set<String>> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[Keys.goals]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    val activeWorkspace: Flow<String?> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[Keys.activeWorkspace]
        }

    suspend fun saveIdentity(
        roles: Set<String>,
        goals: Set<String>,
    ) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.roles] = roles.joinToString(",")
            preferences[Keys.goals] = goals.joinToString(",")
            preferences[Keys.onboardingCompleted] = true
        }
    }

    suspend fun setActiveWorkspace(workspace: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.activeWorkspace] = workspace
        }
    }

    suspend fun resetOnboarding() {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.onboardingCompleted] = false
            preferences.remove(Keys.roles)
            preferences.remove(Keys.goals)
            preferences.remove(Keys.activeWorkspace)
        }
    }
}
