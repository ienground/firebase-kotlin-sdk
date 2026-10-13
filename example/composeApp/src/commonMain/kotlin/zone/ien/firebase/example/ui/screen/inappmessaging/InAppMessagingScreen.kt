package zone.ien.firebase.example.ui.screen.inappmessaging
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
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.inappmessaging.FirebaseInAppMessaging
import zone.ien.firebase.inappmessaging.display.FirebaseInAppMessagingDisplay
import zone.ien.firebase.inappmessaging.display.InAppMessageMetadata
import zone.ien.firebase.inappmessaging.display.InAppMessagingDisplayListener
import zone.ien.firebase.inappmessaging.display.InAppMessageDismissType
import zone.ien.firebase.inappmessaging.display.InAppMessagingDisplayCallbacks
import zone.ien.utils.ui.wrapper.IenRootWrapper
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun InAppMessagingScreen(
    onBack: () -> Unit
) {
    // Crash-safe instance retrieval
    val iamResult = remember { runCatching { FirebaseInAppMessaging.instance } }
    val iam = iamResult.getOrNull()
    val isSupported = iam != null

    // Crash-safe display manager instance retrieval
    val displayResult = remember { runCatching { FirebaseInAppMessagingDisplay.instance } }
    val displayManager = displayResult.getOrNull()
    val isDisplaySupported = displayManager != null

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

    IenRootWrapper {
        AppTheme {
            IenScaffold(
                topBar = {
                    IenTopAppBar(
                        title = { IenText("In-App Messaging") },
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
                    if (!isSupported) {
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
                                text = "In-App Messaging is unavailable on this target or failed to initialize native dependencies.",
                                style = IenTheme.typography.body2,
                                color = Color.Red
                            )
                        }
                    }

                    IenText(
                        text = "Campaign Display Control",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            IenText("Automatic Data Collection", color = if (isSupported) Color.Unspecified else Color.Gray)
                            IenText(
                                "Enable/disable telemetry collection",
                                style = IenTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                        IenSwitch(
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
                            IenText("Suppress Message Display", color = if (isSupported) Color.Unspecified else Color.Gray)
                            IenText(
                                "Silence visual campaigns temporarily",
                                style = IenTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                        IenSwitch(
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

                    IenText(
                        text = "Trigger Event Dispatcher",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) Color.Unspecified else Color.Gray
                    )

                    IenTextField(
                        value = triggerEventName,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { triggerEventName = it },
                        label = "Analytics Trigger Event Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenButton(
                        onClick = {
                            if (triggerEventName.isBlank()) {
                                log("Error: Event name cannot be empty.")
                                return@IenButton
                            }
                            try {
                                log("Triggering event: '$triggerEventName'...")
                                iam?.triggerEvent(triggerEventName)
                                log("Event triggered successfully. Visual messages will display if targeting matches on Firebase Console.")
                            } catch (e: Exception) {
                                log("Trigger failed: ${e.message}")
                            }
                        },
                        state = IenButtonState(enabled = isSupported),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Trigger Event")
                    }

                    IenButton(
                        onClick = {
                            try {
                                log("Binding custom display lifecycle listener...")
                                displayManager?.setCustomDisplayListener(object : InAppMessagingDisplayListener {
                                    override fun displayMessage(message: InAppMessageMetadata, callbacks: InAppMessagingDisplayCallbacks) {
                                        log("Listener callback -> displayed campaign: ID=${message.campaignId}, type=${message.messageType}")
                                        // Must be called when the actual UI is rendered on the screen.
                                        callbacks.impressionDetected()
                                    }
                                })
                                log("Display listener successfully bound to InAppMessagingDisplay!")
                            } catch (e: Exception) {
                                log("Listener registration failed: ${e.message}")
                            }
                        },
                        state = IenButtonState(enabled = isDisplaySupported),
                        
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Register Display Component Listener")
                    }

                    IenText(
                        text = "Actions History Log",
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
                            IenText("No actions logged yet.", color = Color.Gray, style = IenTheme.typography.body2)
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
                        text = "* Testing Verification Guide:\n" +
                                "1. Visual rendering of modal/card campaigns requires targets configured via Firebase Console.\n" +
                                "2. To test immediately on a device/simulator: Retrieve the Firebase Installation ID (available via Installations API) and register it as a 'Test Device' inside In-App Messaging Console.\n" +
                                "3. Foreground transition (backgrounding and reopening the app) is often needed to force check for pending campaigns.",
                        style = IenTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
