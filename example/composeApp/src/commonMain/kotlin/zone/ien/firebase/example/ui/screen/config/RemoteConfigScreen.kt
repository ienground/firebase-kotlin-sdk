package zone.ien.firebase.example.ui.screen.config
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
import zone.ien.utils.ui.foundation.IenSemanticTone

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

    val primaryColor = IenTheme.colors.brand
    val errorColor = IenTheme.colors.danger

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

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Remote Config", fontWeight = FontWeight.Bold) },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) }
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
            IenText(
                text = "Firebase Remote Config Demo",
                
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // Alert Box
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        "⚠️ Warnings and Usage Instructions",
                        fontWeight = FontWeight.Bold,
                        color = IenTheme.colors.danger
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        "1. To test the Real-time Config Update feature locally, change template configurations in the Firebase Console and 'Publish' them.\n" +
                        "2. Firebase Core must be initialized beforehand. Fetch and Activate latency may vary depending on network connectivity.\n" +
                        "3. Calling Fetch more frequently than the minimumFetchInterval can cause THROTTLED exceptions from the server.",
                        
                        color = IenTheme.colors.danger
                    )
                }
            }

            // Defaults Settings Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IenText("1. Local Defaults Configuration", fontWeight = FontWeight.Bold)
                    IenText("Sets local fallback defaults based on code.", )
                    IenButton(
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
                        IenText("Set Defaults")
                    }
                }
            }

            // Settings Configurations Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IenText("2. Config Settings Modification", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenTextField(
                            value = minimumFetchInterval,
                            onValueChange = { minimumFetchInterval = it },
                            label = "Min Fetch Interval (s)",
                            modifier = Modifier.weight(1f)
                        )
                        IenTextField(
                            value = fetchTimeout,
                            onValueChange = { fetchTimeout = it },
                            label = "Timeout (s)",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    IenButton(
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
                        IenText("Update Settings")
                    }
                }
            }

            // Fetch & Activate Operations Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IenText("3. Fetch & Activate Control", fontWeight = FontWeight.Bold)
                    IenText("Fetch the latest remote configuration from server and activate to local template.", )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IenButton(
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
                            IenText("Fetch")
                        }

                        IenButton(
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
                            IenText("Activate")
                        }
                    }

                    IenButton(
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
                        IenText("Fetch and Activate")
                    }

                    IenDivider(modifier = Modifier.padding(vertical = 4.dp))
                    IenText("Last Fetch Status: $lastFetchStatus", )
                    IenText("Last Fetch Time: $lastFetchTime", )
                }
            }

            // Real-time config updates Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IenText("4. Real-time Config Update Listener", fontWeight = FontWeight.Bold)
                    IenText("Detect real-time template update events from the remote backend.", )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IenButton(
                            tone = if (isListening) IenSemanticTone.Danger else IenSemanticTone.Brand,
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
                        ) {
                            IenText(if (isListening) "Disconnect Listener" else "Connect Listener")
                        }

                        IenText(
                            text = if (isListening) "🟢 Connected" else "🔴 Disconnected",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (updatedKeysLog.isNotEmpty()) {
                        IenText(updatedKeysLog,  color = primaryColor)
                    }
                }
            }

            // Config Read Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IenText("5. Config Value Query (Typed Accessors)", fontWeight = FontWeight.Bold)

                    IenTextField(
                        value = testKey,
                        onValueChange = { testKey = it },
                        label = "Query Parameter Key",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenButton(
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
                        IenText("Query Value")
                    }

                    IenDivider(modifier = Modifier.padding(vertical = 4.dp))
                    IenText("String Value: $fetchedStringValue", )
                    IenText("Boolean Value: $fetchedBooleanValue", )
                    IenText("Long Value: $fetchedLongValue", )
                    IenText("Double Value: $fetchedDoubleValue", )
                    IenText("Value Source: $valueSource",  fontWeight = FontWeight.Bold)
                }
            }

            // Console Log Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText("📜 Execution Console Log", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(logMessage, )
                }
            }
        }
    }
}
