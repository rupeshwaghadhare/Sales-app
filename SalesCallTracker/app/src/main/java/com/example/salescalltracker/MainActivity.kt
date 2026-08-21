@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.salescalltracker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.salescalltracker.theme.SalesCallTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SalesCall(
    val number: String,
    val type: String,
    val date: String,
    val duration: String,
    val customerName: String = "",
    val notes: String = "",
    val status: String = "New",
    val followUp: String = ""
)

class MainActivity : ComponentActivity() {

    private val callLogPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                loadCallLogs()
            }
        }

    private var calls by mutableStateOf<List<SalesCall>>(emptyList())

    private val preferences by lazy {
        getSharedPreferences(
            "sales_call_tracker",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SalesCallTrackerTheme {

                CallLogScreen(
                    calls = calls,

                    onLoadCalls = {
                        requestCallLogPermission()
                    },

                    onSaveCall = { updatedCall ->
                        saveCallDetails(updatedCall)
                    }
                )
            }
        }

        requestCallLogPermission()
    }

    private fun requestCallLogPermission() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            loadCallLogs()

        } else {

            callLogPermission.launch(
                Manifest.permission.READ_CALL_LOG
            )
        }
    }

    private fun loadCallLogs() {

        val result = mutableListOf<SalesCall>()

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,

            arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
            ),

            null,
            null,

            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {

            val numberIndex =
                it.getColumnIndex(CallLog.Calls.NUMBER)

            val typeIndex =
                it.getColumnIndex(CallLog.Calls.TYPE)

            val dateIndex =
                it.getColumnIndex(CallLog.Calls.DATE)

            val durationIndex =
                it.getColumnIndex(CallLog.Calls.DURATION)

            while (
                it.moveToNext() &&
                result.size < 100
            ) {

                val number =
                    it.getString(numberIndex)
                        ?: "Unknown"

                val typeValue =
                    it.getInt(typeIndex)

                val type = when (typeValue) {

                    CallLog.Calls.INCOMING_TYPE ->
                        "Incoming"

                    CallLog.Calls.OUTGOING_TYPE ->
                        "Outgoing"

                    CallLog.Calls.MISSED_TYPE ->
                        "Missed"

                    CallLog.Calls.REJECTED_TYPE ->
                        "Rejected"

                    else ->
                        "Other"
                }

                val date =
                    SimpleDateFormat(
                        "dd MMM yyyy, HH:mm",
                        Locale.getDefault()
                    ).format(
                        Date(
                            it.getLong(dateIndex)
                        )
                    )

                val duration =
                    "${it.getLong(durationIndex)} sec"

                val customerName =
                    preferences.getString(
                        "name_$number",
                        ""
                    ) ?: ""

                val notes =
                    preferences.getString(
                        "notes_$number",
                        ""
                    ) ?: ""

                val status =
                    preferences.getString(
                        "status_$number",
                        "New"
                    ) ?: "New"

                val followUp =
                    preferences.getString(
                        "followup_$number",
                        ""
                    ) ?: ""

                result.add(
                    SalesCall(
                        number = number,
                        type = type,
                        date = date,
                        duration = duration,
                        customerName = customerName,
                        notes = notes,
                        status = status,
                        followUp = followUp
                    )
                )
            }
        }

        calls = result
    }

    private fun saveCallDetails(
        call: SalesCall
    ) {

        preferences.edit()

            .putString(
                "name_${call.number}",
                call.customerName
            )

            .putString(
                "notes_${call.number}",
                call.notes
            )

            .putString(
                "status_${call.number}",
                call.status
            )

            .putString(
                "followup_${call.number}",
                call.followUp
            )

            .apply()

        loadCallLogs()
    }
}


@Composable
fun CallLogScreen(
    calls: List<SalesCall>,
    onLoadCalls: () -> Unit,
    onSaveCall: (SalesCall) -> Unit
) {

    var selectedCall by remember {
        mutableStateOf<SalesCall?>(null)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Sales Call Tracker"
                    )
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)

        ) {

            Button(

                onClick = onLoadCalls,

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(
                    text = "Refresh Call Logs"
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Calls: ${calls.size}",
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(

                verticalArrangement =
                    Arrangement.spacedBy(8.dp)

            ) {

                items(calls) { call ->

                    CardContent(
                        call = call,
                        onEdit = {
                            selectedCall = call
                        }
                    )
                }
            }
        }
    }

    selectedCall?.let { call ->

        EditCallDialog(

            call = call,

            onDismiss = {
                selectedCall = null
            },

            onSave = { updatedCall ->

                onSaveCall(updatedCall)

                selectedCall = null
            }
        )
    }
}


@Composable
fun CardContent(
    call: SalesCall,
    onEdit: () -> Unit
) {

    androidx.compose.material3.Card(

        modifier =
            Modifier.fillMaxWidth()

    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)

        ) {

            if (
                call.customerName.isNotBlank()
            ) {

                Text(

                    text = call.customerName,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(
                    text = call.number
                )

            } else {

                Text(

                    text = call.number,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text = "Type: ${call.type}"
            )

            Text(
                text = "Date: ${call.date}"
            )

            Text(
                text = "Duration: ${call.duration}"
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = "Status: ${call.status}"
            )

            if (
                call.followUp.isNotBlank()
            ) {

                Text(
                    text =
                        "Follow-up: ${call.followUp}"
                )
            }

            if (
                call.notes.isNotBlank()
            ) {

                Text(
                    text =
                        "Notes: ${call.notes}"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            OutlinedButton(

                onClick = onEdit,

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(

                    text = if (
                        call.customerName.isBlank() &&
                        call.notes.isBlank()
                    ) {

                        "Add Customer / Notes"

                    } else {

                        "Edit Customer / Follow-up"
                    }
                )
            }
        }
    }
}


@Composable
fun EditCallDialog(
    call: SalesCall,
    onDismiss: () -> Unit,
    onSave: (SalesCall) -> Unit
) {

    var customerName by remember {

        mutableStateOf(
            call.customerName
        )
    }

    var notes by remember {

        mutableStateOf(
            call.notes
        )
    }

    var status by remember {

        mutableStateOf(
            call.status
        )
    }

    var followUp by remember {

        mutableStateOf(
            call.followUp
        )
    }

    var statusExpanded by remember {

        mutableStateOf(false)
    }

    val statuses = listOf(

        "New",

        "Contacted",

        "Interested",

        "Follow-up",

        "Converted",

        "Not Interested"
    )

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = "Sales Details"
            )
        },

        text = {

            Column {

                Text(

                    text = call.number,

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = customerName,

                    onValueChange = {
                        customerName = it
                    },

                    label = {
                        Text(
                            text = "Customer Name"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = status,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text(
                            text = "Lead Status"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    trailingIcon = {

                        TextButton(

                            onClick = {
                                statusExpanded =
                                    !statusExpanded
                            }

                        ) {

                            Text(
                                text = "▼"
                            )
                        }
                    }
                )

                DropdownMenu(

                    expanded = statusExpanded,

                    onDismissRequest = {
                        statusExpanded = false
                    }

                ) {

                    statuses.forEach { item ->

                        DropdownMenuItem(

                            text = {
                                Text(
                                    text = item
                                )
                            },

                            onClick = {

                                status = item

                                statusExpanded = false
                            }
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = followUp,

                    onValueChange = {
                        followUp = it
                    },

                    label = {
                        Text(
                            text = "Follow-up"
                        )
                    },

                    placeholder = {
                        Text(
                            text =
                                "Example: 25 Aug 2026, 10:00 AM"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = notes,

                    onValueChange = {
                        notes = it
                    },

                    label = {
                        Text(
                            text = "Call Notes"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    minLines = 3
                )
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    onSave(

                        call.copy(

                            customerName =
                                customerName.trim(),

                            notes =
                                notes.trim(),

                            status =
                                status,

                            followUp =
                                followUp.trim()
                        )
                    )
                }

            ) {

                Text(
                    text = "Save"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Cancel"
                )
            }
        }
    )
}