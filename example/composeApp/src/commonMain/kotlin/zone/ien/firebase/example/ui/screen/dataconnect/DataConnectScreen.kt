package zone.ien.firebase.example.ui.screen.dataconnect

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import zone.ien.utils.ui.wrapper.M3RootWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataConnectScreen(
    onBack: () -> Unit
) {
    val logs = remember { mutableStateListOf<String>() }

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

    M3RootWrapper {
        AppTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Data Connect") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Text(
                                    text = "←",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
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
                    Text(
                        text = "Connector Configuration",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = serviceName,
                        onValueChange = { serviceName = it },
                        label = { Text("Service Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = connectorName,
                        onValueChange = { connectorName = it },
                        label = { Text("Connector Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Get Core Runtime Instance")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Local Emulator Setup",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = emulatorHost,
                            onValueChange = { emulatorHost = it },
                            label = { Text("Emulator Host") },
                            modifier = Modifier.weight(2f)
                        )

                        OutlinedTextField(
                            value = emulatorPort,
                            onValueChange = { emulatorPort = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Button(
                        onClick = {
                            val dc = dataConnectInstance
                            if (dc == null) {
                                log("Error: Initialize core runtime instance first.")
                                return@Button
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
                        enabled = dataConnectInstance != null,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Bind Emulator")
                    }

                    Text(
                        text = "Active Status: ${if (dataConnectInstance != null) "INITIALIZED" else "NOT READY"} ${if (isEmulatorBound) "(EMULATOR)" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (dataConnectInstance != null) Color(0xFF2E7D32) else Color.Red
                    )

                    Text(
                        text = "Verification Output Log",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (logs.isEmpty()) {
                            Text("No actions performed yet.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        } else {
                            logs.forEach { logLine ->
                                Text("> $logLine", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
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

                    Text(
                        text = "* Developer Guide:\n1. Core runtime wrapper provides initial config mappings and routing context.\n2. In actual production, GraphQL schemas are queried via client codes generated by Firebase CLI.\n3. Make sure postgres local postgresql engine/emulator is alive when binding emulator.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
