package zone.ien.firebase.example.ui.screen.transport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.firebase.transport.Encoding
import zone.ien.firebase.transport.Event
import zone.ien.firebase.transport.Priority
import zone.ien.firebase.transport.Transformer
import zone.ien.firebase.transport.TransportScheduleCallback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatatransportScreen(onNavigateBack: () -> Unit) {
    var payload by remember { mutableStateOf("Hello Datatransport KMP!") }
    var selectedPriority by remember { mutableStateOf(Priority.DEFAULT) }
    val logs = remember { mutableStateListOf<String>() }

    fun log(message: String) {
        logs.add(message)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datatransport API Test") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Configure Telemetry Event",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = payload,
                onValueChange = { payload = it },
                label = { Text("Payload String") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Priority Level",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Priority.values().forEach { priority ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedPriority == priority),
                            onClick = { selectedPriority = priority }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = priority.name, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            log("Creating Event structure...")
                            val event = when (selectedPriority) {
                                Priority.DEFAULT -> Event.ofTelemetry(payload)
                                Priority.HIGHEST -> Event.ofUrgent(payload)
                                Priority.VERY_LOW -> Event.ofTelemetry(100, payload)
                            }
                            log("Event created successfully! Priority: ${event.getPriority()}")
                            log("Event Payload: ${event.getPayload()}")
                            log("Event Code: ${event.getCode()}")

                            log("Checking Encoding structure...")
                            val encoding = Encoding.of("proto")
                            log("Encoding created: ${encoding.name}")
                        } catch (e: Exception) {
                            log("Error during Event/Encoding test: ${e.message}")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Verify Event API")
                }

                Button(
                    onClick = {
                        try {
                            log("Simulating mock Transport scheduler...")
                            val mockTransformer = object : Transformer<String, ByteArray> {
                                override fun apply(input: String): ByteArray = input.encodeToByteArray()
                            }
                            val processed = mockTransformer.apply(payload)
                            log("Transformer applied. Byte size: ${processed.size}")

                            val callback = object : TransportScheduleCallback {
                                override fun onSchedule(error: Exception?) {
                                    if (error != null) {
                                        log("Scheduled callback fired with error: ${error.message}")
                                    } else {
                                        log("Scheduled callback fired successfully with no error.")
                                    }
                                }
                            }
                            callback.onSchedule(null)
                            log("Transport schedule mock run complete.")
                        } catch (e: Exception) {
                            log("Error during Mock scheduling: ${e.message}")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Test Scheduler")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Verification Output Log",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.05f))
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (logs.isEmpty()) {
                    Text(
                        text = "No action logged yet. Click 'Verify Event API' to test KMP contracts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    logs.forEach { logLine ->
                        Text(
                            text = "> $logLine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Button(
                onClick = { logs.clear() },
                colors = ButtonDefaults.textButtonColors(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Clear Log")
            }
        }
    }
}
