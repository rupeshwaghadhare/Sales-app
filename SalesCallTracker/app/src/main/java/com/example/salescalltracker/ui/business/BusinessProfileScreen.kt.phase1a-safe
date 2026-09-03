package com.example.salescalltracker.ui.business

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salescalltracker.data.ActivityRepository

@Composable
fun BusinessProfileScreen(
    repository: ActivityRepository,
    onBack: () -> Unit = {},
    viewModel: BusinessProfileViewModel = viewModel {
        BusinessProfileViewModel(repository)
    },
) {
    val profile by viewModel.businessProfile.collectAsStateWithLifecycle()

    var businessName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }

    LaunchedEffect(profile?.id) {
        profile?.let {
            businessName = it.businessName
            category = it.category
            phone = it.phone.orEmpty()
            whatsapp = it.whatsapp.orEmpty()
            location = it.location.orEmpty()
            description = it.description.orEmpty()
            websiteUrl = it.websiteUrl.orEmpty()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            "🏪 Business Profile",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            if (profile == null)
                "Create a digital profile for your business or for a business you manage."
            else
                "Edit your saved business profile.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = businessName,
            onValueChange = { businessName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Business name *") },
            singleLine = true,
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Business category") },
            placeholder = { Text("Restaurant, Salon, IT, Consultant...") },
            singleLine = true,
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Description") },
            minLines = 3,
        )

        Text(
            "Contact",
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone") },
            singleLine = true,
        )

        OutlinedTextField(
            value = whatsapp,
            onValueChange = { whatsapp = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("WhatsApp") },
            singleLine = true,
        )

        Text(
            "Online presence",
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedTextField(
            value = websiteUrl,
            onValueChange = { websiteUrl = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Website URL") },
            placeholder = { Text("https://...") },
            singleLine = true,
        )

        Text(
            "Location",
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Business location") },
            placeholder = { Text("Address, area, city") },
            minLines = 2,
        )

        Button(
            onClick = {
                viewModel.saveBusiness(
                    businessName = businessName,
                    category = category,
                    phone = phone,
                    whatsapp = whatsapp,
                    location = location,
                    description = description,
                    websiteUrl = websiteUrl,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = businessName.isNotBlank(),
        ) {
            Text(
                if (profile == null) "Create Business Profile"
                else "Save Changes"
            )
        }

        if (profile != null) {
            Text(
                "Business ID: ${profile!!.id}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back")
        }
    }
}
