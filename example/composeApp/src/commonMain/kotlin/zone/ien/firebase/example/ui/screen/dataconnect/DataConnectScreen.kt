package zone.ien.firebase.example.ui.screen.dataconnect
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.FirebaseDataConnect
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.example.util.isIos
import zone.ien.utils.ui.wrapper.IenRootWrapper
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun DataConnectScreen(
    onBack: () -> Unit
) {
    // Check dynamic platform support via safe instantiation probes
    val dynamicProbe = remember { runCatching { FirebaseDataConnect.getInstance(ConnectorConfig("movies", "us-central1", "movie-connector")) } }
    val isSupported = dynamicProbe.isSuccess

    val logs = remember { 
        mutableStateListOf<String>().apply {
            if (dynamicProbe.isFailure) {
                add("Data Connect is NOT supported on this platform: ${dynamicProbe.exceptionOrNull()?.message}")
            } else if (isIos) {
                add("iOS Notice: KMP wrapper serves as a configuration container. Native GraphQL calls must be manually added to the iOS Native project (Swift codebase) due to Swift-only cinterop constraints.")
            }
        }
    }

    var serviceName by remember { mutableStateOf("movies") }
    var locationName by remember { mutableStateOf("us-central1") }
    var connectorName by remember { mutableStateOf("movie-connector") }

    var emulatorHost by remember { mutableStateOf("10.0.2.2") }
    var emulatorPort by remember { mutableStateOf("9399") }

    var dataConnectInstance by remember { mutableStateOf<FirebaseDataConnect?>(null) }
    var isEmulatorBound by remember { mutableStateOf(false) }

    fun log(msg: String) {
        logs.add(msg)
    }

    IenRootWrapper {
        AppTheme {
            IenScaffold(
                topBar = {
                    IenTopAppBar(
                        title = { IenText("Data Connect") },
                        navigationIcon = { IenBackButton(onClick = onBack) }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isIos) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ContinuousRoundedRectangle(8.dp))
                                .background(IenTheme.colors.brandWeak)
                                .padding(12.dp)
                        ) {
                            IenText(
                                text = "ℹ️ iOS cinterop Bridge Notice",
                                style = IenTheme.typography.title2,
                                color = IenTheme.colors.brand
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            IenText(
                                text = "Firebase Data Connect iOS SDK is Swift-only and cannot be linked directly into KMP via cinterop. This KMP wrapper runs in memory-only mode on iOS (acting as a config container). To execute live GraphQL queries, integrate the generated Swift SDK directly inside your native iOS target codebase.",
                                style = IenTheme.typography.body2,
                                color = IenTheme.colors.brand
                            )
                        }
                    } else if (!isSupported) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ContinuousRoundedRectangle(8.dp))
                                .background(Color.Red.copy(alpha = 0.1f))
                                .padding(12.dp)
                        ) {
                            IenText(
                                text = "⚠️ Platform Not Supported",
                                style = IenTheme.typography.title2,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            IenText(
                                text = "Data Connect operations are unavailable on this target due to Swift-only cinterop compilation constraints.",
                                style = IenTheme.typography.body2,
                                color = Color.Red
                            )
                        }
                    }

                    IenText(
                        text = "Connector Configuration",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )

                    IenTextField(
                        value = serviceName,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { serviceName = it },
                        label = "Service Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenTextField(
                        value = locationName,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { locationName = it },
                        label = "Location",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenTextField(
                        value = connectorName,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { connectorName = it },
                        label = "Connector Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenButton(
                        onClick = {
                            try {
                                log("Initializing ConnectorConfig: service=$serviceName, location=$locationName, connector=$connectorName")
                                val config = ConnectorConfig(
                                    service = serviceName,
                                    location = locationName,
                                    connector = connectorName
                                )
                                log("Obtaining FirebaseDataConnect instance...")
                                val dc = FirebaseDataConnect.getInstance(config)
                                dataConnectInstance = dc
                                isEmulatorBound = false
                                log("DataConnect initialized successfully! Config match: service=${dc.config.service}")
                            } catch (e: Exception) {
                                log("Initialization failed: ${e.message}")
                            }
                        },
                        state = IenButtonState(enabled = isSupported),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Get Core Runtime Instance")
                    }

                    IenButton(
                        onClick = {
                            try {
                                log("Obtaining GeneratedConnector.instance...")
                                val connector = zone.ien.firebase.dataconnect.connectors.GeneratedConnector.instance
                                log("GeneratedConnector instance obtained successfully!")
                                log("Binding path verify -> service=${connector.dataConnect.config.service}")
                            } catch (e: Exception) {
                                log("GeneratedConnector verify failed: ${e.message}")
                            }
                        },
                        
                        state = IenButtonState(enabled = isSupported),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Verify Generated Connector Interface")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IenText(
                        text = "Local Emulator Setup",
                        style = IenTheme.typography.title2,
                        color = if (isSupported && dataConnectInstance != null) Color.Unspecified else Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IenTextField(
                            value = emulatorHost,
                            state = IenTextFieldState(enabled = isSupported && dataConnectInstance != null),
                            onValueChange = { emulatorHost = it },
                            label = "Emulator Host",
                            modifier = Modifier.weight(2f)
                        )

                        IenTextField(
                            value = emulatorPort,
                            state = IenTextFieldState(enabled = isSupported && dataConnectInstance != null),
                            onValueChange = { emulatorPort = it },
                            label = "Port",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    IenButton(
                        onClick = {
                            val dc = dataConnectInstance
                            if (dc == null) {
                                log("Error: Initialize core runtime instance first.")
                                return@IenButton
                            }
                            try {
                                val portInt = emulatorPort.toIntOrNull() ?: 9399
                                log("Binding emulator to $emulatorHost:$portInt...")
                                dc.useEmulator(emulatorHost, portInt)
                                isEmulatorBound = true
                                log("Emulator successfully configured.")
                            } catch (e: Exception) {
                                log("Emulator configuration failed: ${e.message}")
                            }
                        },
                        state = IenButtonState(enabled = isSupported && dataConnectInstance != null),
                        
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Bind Emulator")
                    }

                    IenText(
                        text = "Active Status: ${if (dataConnectInstance != null) "INITIALIZED" else "NOT READY"} ${if (isEmulatorBound) "(EMULATOR)" else ""}",
                        style = IenTheme.typography.body1,
                        color = if (dataConnectInstance != null) Color(0xFF2E7D32) else Color.Red
                    )

                    IenText(
                        text = "Verification Output Log",
                        style = IenTheme.typography.title3
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(ContinuousRoundedRectangle(8.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (logs.isEmpty()) {
                            IenText("No actions performed yet.", color = Color.Gray, style = IenTheme.typography.body2)
                        } else {
                            logs.forEach { logLine ->
                                IenText("> $logLine", color = IenTheme.colors.brand, style = IenTheme.typography.body2)
                            }
                        }
                    }

                    IenButton(
                        onClick = { logs.clear() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IenText("Clear Log")
                    }

                    IenText(
                        text = "* Developer Guide:\n1. Core runtime wrapper provides initial config mappings and routing context.\n2. In actual production, GraphQL schemas are queried via client codes generated by Firebase CLI.\n3. Make sure postgres local postgresql engine/emulator is alive when binding emulator.",
                        style = IenTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
