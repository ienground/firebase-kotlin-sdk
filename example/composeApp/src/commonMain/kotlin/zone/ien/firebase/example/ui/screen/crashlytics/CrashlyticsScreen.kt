package zone.ien.firebase.example.ui.screen.crashlytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.firebase.crashlytics.FirebaseCrashlytics
import zone.ien.firebase.crashlytics.ndk.FirebaseCrashlyticsNdk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CrashlyticsScreen(onBack: () -> Unit) {
    val crashlytics = remember { FirebaseCrashlytics.getInstance() }
    val scrollState = rememberScrollState()

    var logMessage by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var customKey by remember { mutableStateOf("") }
    var customValue by remember { mutableStateOf("") }

    val defaultColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    var statusText by remember { mutableStateOf("Idle") }
    var statusColor by remember { mutableStateOf(defaultColor) }

    // Verify NDK support module availability safely
    val ndkStatus = remember {
        try {
            val ndk = FirebaseCrashlyticsNdk.getInstance()
            if (ndk.isNdkCrashCaptureEnabled()) "Enabled (Android NDK Library Present)" else "Disabled"
        } catch (e: UnsupportedOperationException) {
            "Unsupported: ${e.message}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firebase Crashlytics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ Setup Pre-requisites",
                        style = MaterialTheme.typography.titleMedium,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. To view reports, verify your app is registered in Firebase Console.\n" +
                               "2. [Android] Ensure Firebase Crashlytics Gradle plugin is applied in your app module.\n" +
                               "3. [iOS] Make sure to upload dSYM files during the build phase to de-obfuscate stack traces.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Android NDK Support Status Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🤖 Android NDK Crash Capture",
                        style = MaterialTheme.typography.titleSmall,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Status: $ndkStatus",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (ndkStatus.startsWith("Enabled")) primaryColor else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Prerequisites for Android NDK crash capture:\n" +
                               "- Configure CMake/ndk-build in app build.gradle.\n" +
                               "- Enable NDK symbols upload in Gradle via: \n" +
                               "  firebaseCrashlytics { nativeSymbolUploadEnabled true }\n" +
                               "- Apple platforms do not require NDK capture as Crashlytics natively records all C/C++/Swift exceptions.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // User ID Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("User Identifier Configuration", style = MaterialTheme.typography.titleSmall)
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("User ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            crashlytics.setUserId(userId)
                            statusText = "User ID set to: $userId"
                            statusColor = primaryColor
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Set User ID")
                    }
                }
            }

            // Custom Keys Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Custom Metadata Keys", style = MaterialTheme.typography.titleSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = customKey,
                            onValueChange = { customKey = it },
                            label = { Text("Key") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = customValue,
                            onValueChange = { customValue = it },
                            label = { Text("Value") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Button(
                        onClick = {
                            if (customKey.isNotEmpty()) {
                                crashlytics.setCustomKey(customKey, customValue)
                                statusText = "Custom Key '$customKey' set to '$customValue'"
                                statusColor = primaryColor
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Set Custom Key")
                    }
                }
            }

            // Logging & Non-fatal Exceptions Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Logging & Non-Fatal Recording", style = MaterialTheme.typography.titleSmall)
                    OutlinedTextField(
                        value = logMessage,
                        onValueChange = { logMessage = it },
                        label = { Text("Log Message") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (logMessage.isNotEmpty()) {
                                    crashlytics.log(logMessage)
                                    statusText = "Log written: $logMessage"
                                    statusColor = primaryColor
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Write Log")
                        }
                        Button(
                            onClick = {
                                val dummyException = RuntimeException("Mocked Non-Fatal Exception: $logMessage")
                                crashlytics.recordException(dummyException)
                                statusText = "Recorded Non-Fatal Exception!"
                                statusColor = primaryColor
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Record Exception")
                        }
                    }
                }
            }

            // Fatal Crash Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚠️ Force Fatal Crash",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "This will immediately terminate the application to simulate a fatal crash. The report will be sent to the console on next launch.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                    )
                    Button(
                        onClick = {
                            throw RuntimeException("Forced Fatal Crash for testing Firebase Crashlytics KMP wrapper.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Force App Crash")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: $statusText",
                color = statusColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
