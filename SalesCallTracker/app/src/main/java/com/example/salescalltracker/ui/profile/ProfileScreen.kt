package com.example.salescalltracker.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onBack: () -> Unit = {}) {
    var name by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("My Profile", style = MaterialTheme.typography.headlineMedium)
        Text("One profile for students, professionals, freelancers, creators and business owners.")

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Personal & Professional Profile", style = MaterialTheme.typography.titleMedium)

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                )

                TextField(
                    value = profession,
                    onValueChange = { profession = it },
                    label = { Text("Profession / Occupation") },
                    modifier = Modifier
                )

                TextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role: Student / Freelancer / Employee / Business Owner / Creator / Other") },
                    modifier = Modifier
                )

                TextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Skills") },
                    modifier = Modifier
                )

                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("About Me") },
                    minLines = 3,
                    modifier = Modifier
                )
            }
        }

        Button(onClick = {}) {
            Text("Save Profile")
        }

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
