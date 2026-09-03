package com.example.salescalltracker.ui.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.model.BusinessProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class BusinessProfileViewModel(
    private val repository: ActivityRepository,
) : ViewModel() {

    val businessProfile: StateFlow<BusinessProfile?> =
        repository.observeBusinessProfile()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    fun saveBusiness(
        businessName: String,
        category: String,
        phone: String,
        whatsapp: String,
        location: String,
        description: String,
        websiteUrl: String,
    ) {
        if (businessName.isBlank()) return

        val now = System.currentTimeMillis()
        val existing = businessProfile.value

        val profile = BusinessProfile(
            id = existing?.id ?: UUID.randomUUID().toString(),
            businessName = businessName.trim(),
            category = category.trim(),
            phone = phone.trim().takeIf { it.isNotBlank() },
            whatsapp = whatsapp.trim().takeIf { it.isNotBlank() },
            location = location.trim().takeIf { it.isNotBlank() },
            description = description.trim().takeIf { it.isNotBlank() },
            logoUri = existing?.logoUri,
            websiteUrl = websiteUrl.trim().takeIf { it.isNotBlank() },
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
        )

        viewModelScope.launch {
            repository.saveBusinessProfile(profile)
        }
    }
}
