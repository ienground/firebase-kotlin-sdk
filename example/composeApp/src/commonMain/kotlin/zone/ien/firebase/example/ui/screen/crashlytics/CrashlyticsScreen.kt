package zone.ien.firebase.example.ui.screen.crashlytics
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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.firebase.crashlytics.FirebaseCrashlytics
import zone.ien.firebase.crashlytics.ndk.FirebaseCrashlyticsNdk

@Composable
public fun CrashlyticsScreen(onBack: () -> Unit) {
    val crashlytics = remember { FirebaseCrashlytics.getInstance() }
    val scrollState = rememberScrollState()

    var logMessage by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var customKey by remember { mutableStateOf("") }
    var customValue by remember { mutableStateOf("") }

    val defaultColor = IenTheme.colors.textSecondary
    val primaryColor = IenTheme.colors.brand

    var statusText by remember { mutableStateOf("Idle") }
    var statusColor by remember { mutableStateOf(defaultColor) }

    // Verify NDK support module availability safely
    val ndkStatus = remember {
        val ndk = FirebaseCrashlyticsNdk.getInstance()
        if (ndk.isNdkCrashCaptureEnabled()) "Enabled (Android NDK Library Present)" else "Disabled"
    }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Firebase Crashlytics") },
                navigationIcon = { IenBackButton(onClick = onBack) }
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
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "ℹ️ Setup Pre-requisites",
                        style = IenTheme.typography.title2,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "1. To view reports, verify your app is registered in Firebase Console.\n" +
                               "2. [Android] Ensure Firebase Crashlytics Gradle plugin is applied in your app module.\n" +
                               "3. [iOS] Make sure to upload dSYM files during the build phase to de-obfuscate stack traces.",
                        style = IenTheme.typography.body2
                    )
                }
            }

            // Android NDK Support Status Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "🤖 Android NDK Crash Capture",
                        style = IenTheme.typography.title3,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "Status: $ndkStatus",
                        style = IenTheme.typography.body1,
                        color = if (ndkStatus.startsWith("Enabled")) primaryColor else IenTheme.colors.danger
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "Prerequisites for Android NDK crash capture:\n" +
                               "- Configure CMake/ndk-build in app build.gradle.\n" +
                               "- Enable NDK symbols upload in Gradle via: \n" +
                               "  firebaseCrashlytics { nativeSymbolUploadEnabled true }\n" +
                               "- Apple platforms do not require NDK capture as Crashlytics natively records all C/C++/Swift exceptions.",
                        style = IenTheme.typography.body2
                    )
                }
            }

            // User ID Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("User Identifier Configuration", style = IenTheme.typography.title3)
                    IenTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = "User ID",
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenButton(
                        onClick = {
                            crashlytics.setUserId(userId)
                            statusText = "User ID set to: $userId"
                            statusColor = primaryColor
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IenText("Set User ID")
                    }
                }
            }

            // Custom Keys Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Custom Metadata Keys", style = IenTheme.typography.title3)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenTextField(
                            value = customKey,
                            onValueChange = { customKey = it },
                            label = "Key",
                            modifier = Modifier.weight(1f)
                        )
                        IenTextField(
                            value = customValue,
                            onValueChange = { customValue = it },
                            label = "Value",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    IenButton(
                        onClick = {
                            if (customKey.isNotEmpty()) {
                                crashlytics.setCustomKey(customKey, customValue)
                                statusText = "Custom Key '$customKey' set to '$customValue'"
                                statusColor = primaryColor
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IenText("Set Custom Key")
                    }
                }
            }

            // Logging & Non-fatal Exceptions Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Logging & Non-Fatal Recording", style = IenTheme.typography.title3)
                    IenTextField(
                        value = logMessage,
                        onValueChange = { logMessage = it },
                        label = "Log Message",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenButton(
                            onClick = {
                                if (logMessage.isNotEmpty()) {
                                    crashlytics.log(logMessage)
                                    statusText = "Log written: $logMessage"
                                    statusColor = primaryColor
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Write Log")
                        }
                        IenButton(
                            onClick = {
                                val dummyException = RuntimeException("Mocked Non-Fatal Exception: $logMessage")
                                crashlytics.recordException(dummyException)
                                statusText = "Recorded Non-Fatal Exception!"
                                statusColor = primaryColor
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Record Exception")
                        }
                    }
                }
            }

            // Fatal Crash Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText(
                        text = "⚠️ Force Fatal Crash",
                        style = IenTheme.typography.title3,
                        color = IenTheme.colors.danger
                    )
                    IenText(
                        text = "This will immediately terminate the application to simulate a fatal crash. The report will be sent to the console on next launch.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.danger.copy(alpha = 0.8f)
                    )
                    IenButton(
                        onClick = {
                            throw RuntimeException("Forced Fatal Crash for testing Firebase Crashlytics KMP wrapper.")
                        },
                        
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        IenText("Force App Crash")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            IenText(
                text = "Status: $statusText",
                color = statusColor,
                style = IenTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
