package zone.ien.firebase.example.ui.screen.inappmessaging

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
import androidx.compose.material3.Switch
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
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.inappmessaging.FirebaseInAppMessaging
import zone.ien.utils.ui.wrapper.M3RootWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppMessagingScreen(
    onBack: () -> Unit
) {
    // Crash-safe instance retrieval
    val iamResult = remember { runCatching { FirebaseInAppMessaging.instance } }
    val iam = iamResult.getOrNull()
    val isSupported = iam != null

    val logs = remember { 
        mutableStateListOf<String>().apply {
            if (iamResult.isFailure) {
                add("In-App Messaging is NOT supported on this platform: ${iamResult.exceptionOrNull()?.message}")
            }
        }
    }

    var isDataCollectionEnabled by remember { mutableStateOf(iam?.isAutomaticDataCollectionEnabled ?: false) }
    var isSuppressed by remember { mutableStateOf(iam?.areMessagesSuppressed ?: false) }
    var triggerEventName by remember { mutableStateOf("test_campaign_trigger") }

    fun log(msg: String) {
        logs.add(msg)
    }

    M3RootWrapper {
        AppTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("In-App Messaging") },
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
                                text = "In-App Messaging is unavailable on this target due to Swift-only cinterop compilation constraints.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }

                    Text(
                        text = "Campaign Display Control",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Automatic Data Collection", color = if (isSupported) Color.Unspecified else Color.Gray)
                            Text(
                                "Enable/disable telemetry collection",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = isDataCollectionEnabled,
                            enabled = isSupported,
                            onCheckedChange = { checked ->
                                try {
                                    iam?.isAutomaticDataCollectionEnabled = checked
                                    isDataCollectionEnabled = checked
                                    log("Automatic Data Collection set to: $checked")
                                } catch (e: Exception) {
                                    log("Failed to set collection: ${e.message}")
                                }
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Suppress Message Display", color = if (isSupported) Color.Unspecified else Color.Gray)
                            Text(
                                "Silence visual campaigns temporarily",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = isSuppressed,
                            enabled = isSupported,
                            onCheckedChange = { checked ->
                                try {
                                    iam?.areMessagesSuppressed = checked
                                    isSuppressed = checked
                                    log("Messages suppressed set to: $checked")
                                } catch (e: Exception) {
                                    log("Failed to set suppression: ${e.message}")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Trigger Event Dispatcher",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )

                    OutlinedTextField(
                        value = triggerEventName,
                        enabled = isSupported,
                        onValueChange = { triggerEventName = it },
                        label = { Text("Analytics Trigger Event Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (triggerEventName.isBlank()) {
                                log("Error: Event name cannot be empty.")
                                return@Button
                            }
                            try {
                                log("Triggering event: '$triggerEventName'...")
                                iam?.triggerEvent(triggerEventName)
                                log("Event triggered successfully. Visual messages will display if targeting matches on Firebase Console.")
                            } catch (e: Exception) {
                                log("Trigger failed: ${e.message}")
                            }
                        },
                        enabled = isSupported,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Trigger Event")
                    }

                    Text(
                        text = "Actions History Log",
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
                            Text("No actions logged yet.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
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
                        text = "* Testing Verification Guide:\n" +
                                "1. Visual rendering of modal/card campaigns requires targets configured via Firebase Console.\n" +
                                "2. To test immediately on a device/simulator: Retrieve the Firebase Installation ID (available via Installations API) and register it as a 'Test Device' inside In-App Messaging Console.\n" +
                                "3. Foreground transition (backgrounding and reopening the app) is often needed to force check for pending campaigns.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
