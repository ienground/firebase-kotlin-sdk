package zone.ien.firebase.example.ui.screen.config

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import zone.ien.firebase.remoteconfig.FirebaseRemoteConfig
import zone.ien.firebase.remoteconfig.FirebaseRemoteConfigSettings
import zone.ien.firebase.remoteconfig.configUpdates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteConfigScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var logMessage by remember { mutableStateOf("Ready to execute Remote Config operations.") }

    var fetchedStringValue by remember { mutableStateOf("N/A") }
    var fetchedBooleanValue by remember { mutableStateOf("N/A") }
    var fetchedLongValue by remember { mutableStateOf("N/A") }
    var fetchedDoubleValue by remember { mutableStateOf("N/A") }
    var valueSource by remember { mutableStateOf("N/A") }

    var lastFetchStatus by remember { mutableStateOf("N/A") }
    var lastFetchTime by remember { mutableStateOf("N/A") }

    var minimumFetchInterval by remember { mutableStateOf("60") }
    var fetchTimeout by remember { mutableStateOf("15") }

    var isListening by remember { mutableStateOf(false) }
    var updatedKeysLog by remember { mutableStateOf("") }
    var listenerJob by remember { mutableStateOf<Job?>(null) }

    var testKey by remember { mutableStateOf("welcome_message") }

    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    fun updateConfigInfo() {
        try {
            val config = FirebaseRemoteConfig.instance
            val info = config.getInfo()
            lastFetchStatus = info.lastFetchStatus.name
            lastFetchTime = "${info.fetchTimeMillis} ms"
        } catch (e: Exception) {
            logMessage = "Failed to update configuration info: ${e.message}"
        }
    }

    LaunchedEffect(Unit) {
        updateConfigInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Config", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Firebase Remote Config Demo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // Alert Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "⚠️ Warnings and Usage Instructions",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1. To test the Real-time Config Update feature locally, change template configurations in the Firebase Console and 'Publish' them.\n" +
                        "2. Firebase Core must be initialized beforehand. Fetch and Activate latency may vary depending on network connectivity.\n" +
                        "3. Calling Fetch more frequently than the minimumFetchInterval can cause THROTTLED exceptions from the server.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Defaults Settings Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Local Defaults Configuration", fontWeight = FontWeight.Bold)
                    Text("Sets local fallback defaults based on code.", fontSize = 12.sp)
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val defaults = mapOf(
                                        "welcome_message" to "Hello World from local default!",
                                        "is_feature_enabled" to true,
                                        "app_theme_color" to 0xFFFFFF00L
                                    )
                                    FirebaseRemoteConfig.instance.setDefaults(defaults)
                                    logMessage = "Local defaults set successfully!\nKey: welcome_message -> 'Hello World from local default!'\nKey: is_feature_enabled -> true"
                                } catch (e: Exception) {
                                    logMessage = "Failed to set defaults: ${e.message}"
                                }
                            }
                        }
                    ) {
                        Text("Set Defaults")
                    }
                }
            }

            // Settings Configurations Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("2. Config Settings Modification", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = minimumFetchInterval,
                            onValueChange = { minimumFetchInterval = it },
                            label = { Text("Min Fetch Interval (s)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = fetchTimeout,
                            onValueChange = { fetchTimeout = it },
                            label = { Text("Timeout (s)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val settings = FirebaseRemoteConfigSettings.Builder()
                                        .setMinimumFetchIntervalInSeconds(minimumFetchInterval.toLongOrNull() ?: 60L)
                                        .setFetchTimeoutInSeconds(fetchTimeout.toLongOrNull() ?: 15L)
                                        .build()
                                    FirebaseRemoteConfig.instance.setSettings(settings)
                                    logMessage = "Config Settings updated successfully."
                                    updateConfigInfo()
                                } catch (e: Exception) {
                                    logMessage = "Failed to set settings: ${e.message}"
                                }
                            }
                        }
                    ) {
                        Text("Update Settings")
                    }
                }
            }

            // Fetch & Activate Operations Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("3. Fetch & Activate Control", fontWeight = FontWeight.Bold)
                    Text("Fetch the latest remote configuration from server and activate to local template.", fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        logMessage = "Fetching remote config..."
                                        val success = FirebaseRemoteConfig.instance.fetch()
                                        logMessage = "Fetch Completed! Status: $success"
                                        updateConfigInfo()
                                    } catch (e: Exception) {
                                        logMessage = "Fetch Failed: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fetch")
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        logMessage = "Activating config..."
                                        val changed = FirebaseRemoteConfig.instance.activate()
                                        logMessage = "Activate Completed! Template changed? $changed"
                                        updateConfigInfo()
                                    } catch (e: Exception) {
                                        logMessage = "Activate Failed: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Activate")
                        }
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    logMessage = "Fetching and Activating config..."
                                    val success = FirebaseRemoteConfig.instance.fetchAndActivate()
                                    logMessage = "Fetch & Activate Completed! Success? $success"
                                    updateConfigInfo()
                                } catch (e: Exception) {
                                    logMessage = "Fetch & Activate Failed: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fetch and Activate")
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("Last Fetch Status: $lastFetchStatus", fontSize = 12.sp)
                    Text("Last Fetch Time: $lastFetchTime", fontSize = 12.sp)
                }
            }

            // Real-time config updates Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("4. Real-time Config Update Listener", fontWeight = FontWeight.Bold)
                    Text("Detect real-time template update events from the remote backend.", fontSize = 12.sp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                if (isListening) {
                                    listenerJob?.cancel()
                                    isListening = false
                                    logMessage = "Real-time update listener detached."
                                } else {
                                    isListening = true
                                    logMessage = "Listening for remote config changes in real-time..."
                                    listenerJob = coroutineScope.launch {
                                        FirebaseRemoteConfig.instance.configUpdates
                                            .catch { e ->
                                                logMessage = "Error in stream: ${e.message}"
                                                isListening = false
                                            }
                                            .collect { update ->
                                                val keys = update.updatedKeys.joinToString(", ")
                                                updatedKeysLog = "Updated keys: $keys"
                                                logMessage = "Received update event! Updated keys: $keys. You should fetch & activate to apply."
                                            }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isListening) errorColor else primaryColor
                            )
                        ) {
                            Text(if (isListening) "Disconnect Listener" else "Connect Listener")
                        }

                        Text(
                            text = if (isListening) "🟢 Connected" else "🔴 Disconnected",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (updatedKeysLog.isNotEmpty()) {
                        Text(updatedKeysLog, fontSize = 12.sp, color = primaryColor)
                    }
                }
            }

            // Config Read Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("5. Config Value Query (Typed Accessors)", fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = testKey,
                        onValueChange = { testKey = it },
                        label = { Text("Query Parameter Key") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            try {
                                val value = FirebaseRemoteConfig.instance.getValue(testKey)
                                fetchedStringValue = value.asString()
                                fetchedBooleanValue = "${value.asBoolean()}"
                                fetchedLongValue = "${value.asLong()}"
                                fetchedDoubleValue = "${value.asDouble()}"
                                valueSource = value.source.name
                                logMessage = "Key '$testKey' queried successfully."
                            } catch (e: Exception) {
                                logMessage = "Failed to query key: ${e.message}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Query Value")
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("String Value: $fetchedStringValue", fontSize = 13.sp)
                    Text("Boolean Value: $fetchedBooleanValue", fontSize = 13.sp)
                    Text("Long Value: $fetchedLongValue", fontSize = 13.sp)
                    Text("Double Value: $fetchedDoubleValue", fontSize = 13.sp)
                    Text("Value Source: $valueSource", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Console Log Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📜 Execution Console Log", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(logMessage, fontSize = 12.sp)
                }
            }
        }
    }
}
