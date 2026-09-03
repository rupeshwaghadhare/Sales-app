package com.example.salescalltracker.ui.business

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salescalltracker.data.ActivityRepository

@Composable
fun BusinessProfileScreen(
    repository: ActivityRepository,
    onBack: () -> Unit = {},
    onChatClick: () -> Unit = {},
    viewModel: BusinessProfileViewModel = viewModel {
        BusinessProfileViewModel(repository)
    },
) {
    val context = LocalContext.current
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

    fun openUrl(url: String) {
        runCatching {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
        }
    }

    fun callBusiness() {
        if (phone.isBlank()) return

        runCatching {
            context.startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${phone.trim()}")
                )
            )
        }
    }

    fun openWhatsApp() {
        if (whatsapp.isBlank()) return

        val number = whatsapp.filter { it.isDigit() }

        if (number.isNotBlank()) {
            openUrl("https://wa.me/$number")
        }
    }

    fun shareProfile() {
        val id = profile?.id ?: return

        val text = buildString {
            append(businessName.ifBlank { "Business Profile" })

            if (category.isNotBlank()) {
                append("\n")
                append(category)
            }

            if (description.isNotBlank()) {
                append("\n\n")
                append(description)
            }

            if (location.isNotBlank()) {
                append("\n\nLocation: ")
                append(location)
            }

            append("\n\nBusiness ID: ")
            append(id)
        }

        runCatching {
            context.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    },
                    "Share Business Profile"
                )
            )
        }
    }

    fun openLocation() {
        if (location.isBlank()) return

        val query = Uri.encode(location.trim())
        openUrl("geo:0,0?q=$query")
    }

    val scrollState = androidx.compose.foundation.rememberScrollState()

    val collapsed by androidx.compose.runtime.remember {
        androidx.compose.runtime.derivedStateOf {
            scrollState.value > 180
        }
    }

    val heroAlpha by animateFloatAsState(
        targetValue = if (collapsed) 0.72f else 1f,
        label = "heroAlpha",
    )

    val heroHeight by animateDpAsState(
        targetValue = if (collapsed) 170.dp else 280.dp,
        label = "heroHeight",
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedVisibility(
                visible = collapsed,
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {

                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = "Business",
                                modifier = Modifier.padding(10.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = businessName.ifBlank { "Your Business" },
                                style = MaterialTheme.typography.titleMedium,
                            )

                            Text(
                                text = category.ifBlank { "Business Profile" },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heroHeight)
                    .alpha(heroAlpha)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    Surface(
                        modifier = Modifier
                            .size(if (collapsed) 76.dp else 100.dp)
                            .clip(CircleShape),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Business logo",
                            modifier = Modifier.padding(
                                if (collapsed) 18.dp else 25.dp
                            ),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = businessName.ifBlank { "Your Business" },
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    if (category.isNotBlank()) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            text = "Verified Business Profile",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 120.dp,
                ),
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Text(
                                text = "About",
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Text(
                                text = description.ifBlank {
                                    "Add a short description to tell people what this business does."
                                },
                                color = if (description.isBlank()) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Explore",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(
                            listOf(
                                "Products",
                                "Services",
                                "Offers",
                                "Posts",
                                "Events",
                                "Reviews",
                            )
                        ) { section ->

                            Card(
                                modifier = Modifier.size(
                                    width = 130.dp,
                                    height = 90.dp,
                                ),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.secondaryContainer
                                ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    verticalArrangement =
                                        Arrangement.Center,
                                ) {
                                    Text(
                                        text = section,
                                        style =
                                            MaterialTheme.typography.titleMedium,
                                    )

                                    Text(
                                        text = "Explore",
                                        style =
                                            MaterialTheme.typography.labelSmall,
                                        color =
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {

                            Text(
                                text = "Contact & Location",
                                style = MaterialTheme.typography.titleLarge,
                            )

                            if (location.isNotBlank()) {
                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically,
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Default.LocationOn,
                                        contentDescription = "Location",
                                    )

                                    Text(
                                        text = location,
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }

                            if (websiteUrl.isNotBlank()) {
                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically,
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Default.Language,
                                        contentDescription = "Website",
                                    )

                                    Text(
                                        text = websiteUrl,
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }

                            if (
                                location.isBlank() &&
                                websiteUrl.isBlank()
                            ) {
                                Text(
                                    text = "Add your location and website.",
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Business Information",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                item {
                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Business name *") },
                        singleLine = true,
                    )
                }

                item {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Category") },
                        placeholder = {
                            Text("Restaurant, Salon, IT, Consultant...")
                        },
                        singleLine = true,
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("About") },
                        minLines = 3,
                    )
                }

                item {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Phone") },
                        singleLine = true,
                    )
                }

                item {
                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("WhatsApp") },
                        singleLine = true,
                    )
                }

                item {
                    OutlinedTextField(
                        value = websiteUrl,
                        onValueChange = { websiteUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Website") },
                        placeholder = { Text("https://...") },
                        singleLine = true,
                    )
                }

                item {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Location") },
                        placeholder = {
                            Text("Address, area, city")
                        },
                        minLines = 2,
                    )
                }

                item {
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
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Save",
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(
                            if (profile == null) {
                                "Create Business Profile"
                            } else {
                                "Save Changes"
                            }
                        )
                    }
                }

                item {
                    if (profile != null) {
                        Text(
                            text = "Business ID: ${profile!!.id}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        /*
         * FLOATING ACTION BAR
         */
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                ),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp,
            shadowElevation = 10.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                OutlinedButton(
                    onClick = onChatClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Chat",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text("Chat")
                }

                OutlinedButton(
                    onClick = ::callBusiness,
                    modifier = Modifier.weight(1f),
                    enabled = phone.isNotBlank(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text("Call")
                }

                OutlinedButton(
                    onClick = ::openWhatsApp,
                    modifier = Modifier.weight(1f),
                    enabled = whatsapp.isNotBlank(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text("WA")
                }

                OutlinedButton(
                    onClick = ::shareProfile,
                    enabled = profile != null,
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                    )
                }
            }
        }
    }
}
