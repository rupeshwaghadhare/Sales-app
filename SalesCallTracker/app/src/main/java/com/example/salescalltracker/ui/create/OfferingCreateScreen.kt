package com.example.salescalltracker.ui.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class SelectedMedia(
    val type: String,
    val uri: Uri? = null,
    val link: String? = null,
)

@Composable
fun OfferingCreateScreen(
    type: String,
    onBack: () -> Unit = {},
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    val mediaItems = remember { mutableStateListOf<SelectedMedia>() }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            mediaItems.add(SelectedMedia("IMAGE", uri = it))
        }
    }

    val videoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            mediaItems.add(SelectedMedia("VIDEO", uri = it))
        }
    }

    val animationPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            mediaItems.add(SelectedMedia("ANIMATION", uri = it))
        }
    }

    val documentPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            mediaItems.add(SelectedMedia("DOCUMENT", uri = it))
        }
    }

    val displayType = when (type) {
        "PRODUCT" -> "🛍️ Product"
        "SERVICE" -> "🧰 Service"
        "KNOWLEDGE" -> "📚 Knowledge"
        "FRANCHISE" -> "🏷️ Franchise"
        "CAMPAIGN" -> "📣 Campaign"
        "EVENT" -> "🎪 Event"
        else -> "Create"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                displayType,
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        item {
            Text(
                "Create this for yourself or on behalf of another person or business.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Name / Title") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (
            type == "PRODUCT" ||
            type == "SERVICE" ||
            type == "KNOWLEDGE" ||
            type == "FRANCHISE"
        ) {
            item {
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (₹)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            Text(
                "Media & More Details",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = {
                                imagePicker.launch("image/*")
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("📷 Photo")
                        }

                        OutlinedButton(
                            onClick = {
                                videoPicker.launch("video/*")
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("🎥 Video")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = {
                                animationPicker.launch("image/gif")
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("🎞️ Animation")
                        }

                        OutlinedButton(
                            onClick = {
                                documentPicker.launch("application/pdf")
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("📄 Document")
                        }
                    }

                    TextField(
                        value = link,
                        onValueChange = { link = it },
                        label = { Text("🔗 Website / Portfolio / Demo Link") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    Button(
                        onClick = {
                            val cleanLink = link.trim()

                            if (cleanLink.isNotBlank()) {
                                mediaItems.add(
                                    SelectedMedia(
                                        type = "LINK",
                                        link = cleanLink,
                                    )
                                )
                                link = ""
                            }
                        },
                        enabled = link.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Add Link")
                    }
                }
            }
        }

        item {
            if (mediaItems.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            "Added Details",
                            style = MaterialTheme.typography.titleMedium,
                        )

                        mediaItems.forEachIndexed { index, media ->
                            Text(
                                when (media.type) {
                                    "IMAGE" -> "📷 Photo ${index + 1}"
                                    "VIDEO" -> "🎥 Video ${index + 1}"
                                    "ANIMATION" -> "🎞️ Animation ${index + 1}"
                                    "DOCUMENT" -> "📄 Document ${index + 1}"
                                    "LINK" -> "🔗 ${media.link}"
                                    else -> media.type
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    // Room/media persistence will be connected in the next step.
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save Draft")
            }
        }

        item {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Back")
            }
        }
    }
}
