package zone.ien.firebase.example.ui.screen.transport

import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.feedback.IenCircularProgressIndicator
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.wrapper.IenRootWrapper
import zone.ien.utils.ui.dialog.IenConfirmDialog
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.TextStyle

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
import androidx.compose.foundation.verticalScroll
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
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun DatatransportScreen(onNavigateBack: () -> Unit) {
    val isSupported = true
    var payload by remember { mutableStateOf("Hello Datatransport KMP!") }
    var selectedPriority by remember { mutableStateOf(Priority.DEFAULT) }
    val logs = remember {
        mutableStateListOf<String>().apply {
            if (isIos) {
                add("Datatransport is running in Simulation Mode (iOS Memory-based).")
            }
        }
    }

    fun log(message: String) {
        logs.add(message)
    }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Datatransport API Test") },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) }
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
            if (isIos) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ContinuousRoundedRectangle(8.dp))
                        .background(IenTheme.colors.brand.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    IenText(
                        text = "⚠️ Simulation Mode (iOS Memory-based)",
                        style = IenTheme.typography.title2,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    IenText(
                        text = "Datatransport runs in memory-based simulation mode on iOS. Telemetry event mappings and scheduler callback loops are verified offline without sending network payloads.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.brand
                    )
                }
            }

            IenText(
                text = "Configure Telemetry Event",
                style = IenTheme.typography.title2,
                color = if (isSupported) Color.Unspecified else Color.Gray
            )

            IenTextField(
                value = payload,
                state = IenTextFieldState(enabled = isSupported),
                onValueChange = { payload = it },
                label = "Payload String",
                modifier = Modifier.fillMaxWidth()
            )

            IenText(
                text = "Priority Level",
                style = IenTheme.typography.body1,
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
                        IenDotCheckbox(checked = (selectedPriority == priority), onCheckedChange = {
                            if (it) {
                                selectedPriority = priority
                            }
                        })
                        Spacer(modifier = Modifier.width(4.dp))
                        IenText(
                            text = priority.name,
                            style = IenTheme.typography.body2,
                            color = if (isSupported) Color.Unspecified else Color.Gray
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IenButton(
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
                    state = IenButtonState(enabled = isSupported),
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Verify Event API")
                }

                IenButton(
                    onClick = {
                        try {
                            log("Simulating mock Transport scheduler...")
                            val mockTransformer = object : Transformer<String, ByteArray> {
                                override fun apply(input: String): ByteArray =
                                    input.encodeToByteArray()
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
                    state = IenButtonState(enabled = isSupported),
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Verify Callback API")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText(
                        text = "CCT Destination Verification",
                        fontWeight = FontWeight.Bold,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )
                    IenButton(
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
                                val testDestination =
                                    CCTDestination("https://test.endpoint.com", "test-api-key-xyz")
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
                        state = IenButtonState(enabled = isSupported),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Verify CCT Destination")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IenButton(
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
                    state = IenButtonState(enabled = isSupported),

                    modifier = Modifier.fillMaxWidth()
                ) {
                    IenText("Verify Transport Runtime Pipeline")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            IenText(
                text = "Verification Output Log",
                style = IenTheme.typography.title2
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(ContinuousRoundedRectangle(8.dp))
                    .background(Color.Black.copy(alpha = 0.05f))
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (logs.isEmpty()) {
                    IenText(
                        text = "No action logged yet. Click 'Verify Event API' to test KMP contracts.",
                        style = IenTheme.typography.body2,
                        color = Color.Gray
                    )
                } else {
                    logs.forEach { logLine ->
                        IenText(
                            text = "> $logLine",
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.brand,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            IenButton(
                onClick = { logs.clear() },
                modifier = Modifier.align(Alignment.End)
            ) {
                IenText("Clear Log")
            }
        }
    }
}
