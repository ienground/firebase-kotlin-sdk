package zone.ien.firebase.example.ui.screen.transport

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zone.ien.firebase.example.util.isIos
import zone.ien.firebase.transport.Encoding
import zone.ien.firebase.transport.Event
import zone.ien.firebase.transport.Priority
import zone.ien.firebase.transport.Transformer
import zone.ien.firebase.transport.TransportScheduleCallback
import zone.ien.firebase.transport.cct.CCTDestination
import zone.ien.firebase.transport.runtime.TransportRuntime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatatransportScreen(onNavigateBack: () -> Unit) {
    val isSupported = !isIos
    var payload by remember { mutableStateOf("Hello Datatransport KMP!") }
    var selectedPriority by remember { mutableStateOf(Priority.DEFAULT) }
    val logs = remember { 
        mutableStateListOf<String>().apply {
            if (!isSupported) {
                add("Datatransport is NOT supported on this platform.")
            }
        }
    }

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
                }
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
            if (!isSupported) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "⚠️ Platform Not Supported",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Datatransport is unavailable on this target due to Swift-only cinterop compilation constraints.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }
            }

            Text(
                text = "Configure Telemetry Event",
                style = MaterialTheme.typography.titleMedium,
                color = if (isSupported) Color.Unspecified else Color.Gray
            )

            OutlinedTextField(
                value = payload,
                enabled = isSupported,
                onValueChange = { payload = it },
                label = { Text("Payload String") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Priority Level",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSupported) Color.Unspecified else Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Priority.entries.forEach { priority ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(enabled = isSupported) {
                            if (isSupported) selectedPriority = priority
                        }
                    ) {
                        RadioButton(
                            selected = (selectedPriority == priority),
                            enabled = isSupported,
                            onClick = { selectedPriority = priority }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = priority.name, 
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSupported) Color.Unspecified else Color.Gray
                        )
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
                                else -> Event.ofTelemetry(payload)
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
                    enabled = isSupported,
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
                                                            log("Scheduled callback completed successfully!")
                                                        }
                                                    }
                                                }
                            callback.onSchedule(null)
                        } catch (e: Exception) {
                            log("Error during Transport callback test: ${e.message}")
                        }
                    },
                    enabled = isSupported,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Verify Callback API")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "CCT Destination Verification", 
                        fontWeight = FontWeight.Bold,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )
                    Button(
                        onClick = {
                            try {
                                log("Fetching CCTDestination.INSTANCE...")
                                val cct = CCTDestination.INSTANCE
                                log("Destination Name: ${cct.name}")
                                log("Destination Endpoint: ${cct.endpoint}")
                                log("Supported Encodings: ${cct.supportedEncodings.joinToString { it.name }}")

                                log("Fetching CCTDestination.LEGACY_INSTANCE...")
                                val legacy = CCTDestination.LEGACY_INSTANCE
                                log("Legacy Name: ${legacy.name}")
                                log("Legacy Endpoint: ${legacy.endpoint}")
                                log("Legacy APIKey: ${legacy.apiKey}")

                                log("Testing serialization...")
                                val testDestination = CCTDestination("https://test.endpoint.com", "test-api-key-xyz")
                                val bytes = testDestination.asByteArray()
                                if (bytes != null) {
                                    log("Serialized byte size: ${bytes.size}")
                                    val parsed = CCTDestination.fromByteArray(bytes)
                                    log("Parsed Destination Endpoint: ${parsed.endpoint}")
                                    log("Parsed Destination APIKey: ${parsed.apiKey}")
                                } else {
                                    log("Serialization returned null")
                                }
                            } catch (e: Exception) {
                                log("CCTDestination verification failed: ${e.message}")
                            }
                        },
                        enabled = isSupported,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify CCT Destination")
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
                            log("Getting TransportRuntime instance...")
                            val runtime = TransportRuntime.getInstance()
                            log("TransportRuntime successfully obtained!")

                            log("Obtaining TransportFactory via CCTDestination...")
                            val factory = runtime.newFactory(CCTDestination.INSTANCE)
                            log("TransportFactory obtained successfully!")

                            log("Creating Telemetry Transport instance...")
                            val transport = factory.getTransport(
                                "cct-test-topic",
                                String::class,
                                Encoding.of("proto"),
                                object : Transformer<String, ByteArray> {
                                    override fun apply(input: String): ByteArray {
                                        return input.encodeToByteArray()
                                    }
                                }
                            )

                            log("Scheduling test telemetry event...")
                            val event = Event.ofTelemetry(payload)
                            transport.schedule(event, object : TransportScheduleCallback {
                                override fun onSchedule(error: Exception?) {
                                    if (error != null) {
                                        log("Telemetry scheduler finished with error: ${error.message}")
                                    } else {
                                        log("Telemetry scheduler completed event routing successfully!")
                                    }
                                }
                            })
                        } catch (e: Exception) {
                            log("TransportRuntime verification failed: ${e.message}")
                        }
                    },
                    enabled = isSupported,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verify Transport Runtime Pipeline")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Verification Output Log",
                style = MaterialTheme.typography.titleMedium
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
