@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.salescalltracker


import android.Manifest
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.salescalltracker.data.ActivityRepository
import com.example.salescalltracker.data.AppDatabase
import com.example.salescalltracker.data.RoomActivityRepository
import com.example.salescalltracker.theme.SalesCallTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SalesCall(
    val number: String,
    val type: String,
    val date: String,
    val duration: String
)

class MainActivity : ComponentActivity() {

    private val activityRepository: ActivityRepository by lazy {
        RoomActivityRepository(
            AppDatabase.getInstance(applicationContext)
        )
    }
    private var calls by mutableStateOf<List<SalesCall>>(emptyList())

    private val callLogPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                loadCallLogs()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
    SalesCallTrackerTheme {
        MainNavigation(
            repository = activityRepository,
            calls = calls,
            onLoadCalls = { requestCallLogPermission() }
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
            callLogPermission.launch(Manifest.permission.READ_CALL_LOG)
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

            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext() && result.size < 100) {

                val number =
                    it.getString(numberIndex) ?: "Unknown"

                val typeValue =
                    it.getInt(typeIndex)

                val type = when (typeValue) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    CallLog.Calls.REJECTED_TYPE -> "Rejected"
                    else -> "Other"
                }

                val date =
                    SimpleDateFormat(
                        "dd MMM yyyy, HH:mm",
                        Locale.getDefault()
                    ).format(
                        Date(it.getLong(dateIndex))
                    )

                val duration =
                    "${it.getLong(durationIndex)} sec"

                result.add(
                    SalesCall(
                        number = number,
                        type = type,
                        date = date,
                        duration = duration
                    )
                )
            }
        }

        calls = result
    }
}

@Composable
fun CallLogScreen(
    calls: List<SalesCall>,
    onLoadCalls: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sales Call Tracker")
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

            Text(
                text = "Sales Call Tracker is running",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onLoadCalls,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Refresh Call Logs")
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Calls found: ${calls.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(calls) { call ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = call.number,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Type: ${call.type}")
                            Text("Date: ${call.date}")
                            Text("Duration: ${call.duration}")
                        }
                    }
                }
            }
        }
    }
}







