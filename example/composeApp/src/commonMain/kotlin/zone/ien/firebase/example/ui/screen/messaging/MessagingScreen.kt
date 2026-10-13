package zone.ien.firebase.example.ui.screen.messaging
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.messaging.FirebaseMessaging
import zone.ien.firebase.messaging.directboot.FirebaseMessagingDirectBoot
import zone.ien.utils.ui.wrapper.IenRootWrapper
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun MessagingScreen(
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var tokenText by remember { mutableStateOf("No token retrieved yet.") }
    val logs = remember { mutableStateListOf<String>() }

    fun log(msg: String) {
        logs.add(msg)
    }

    IenRootWrapper {
        AppTheme {
            IenScaffold(
                topBar = {
                    IenTopAppBar(
                        title = { IenText("Firebase Messaging") },
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
                    IenText(
                        text = "FCM Client SDK Verification Panel",
                        style = IenTheme.typography.title2
                    )

                    IenText(
                        text = "This screen acts as a developer utility to request and revoke registration tokens or manage topic subscriptions using the KMP wrappers.",
                        style = IenTheme.typography.body1
                    )

                    // Display Current Token Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ContinuousRoundedRectangle(8.dp))
                            .background(IenTheme.colors.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        IenText("Current Token:", style = IenTheme.typography.label2)
                        Spacer(modifier = Modifier.height(4.dp))
                        IenText(
                            text = tokenText,
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textSecondary
                        )
                    }

                    // Token Actions
                    IenButton(
                        onClick = {
                            scope.launch {
                                try {
                                    log("Requesting FCM registration token...")
                                    val fcmToken = FirebaseMessaging.getInstance().getToken()
                                    tokenText = fcmToken ?: "Token returned null."
                                    log("Success: Token fetched.")
                                } catch (e: Exception) {
                                    log("Failed: ${e.message}")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Get FCM Token")
                    }

                    IenButton(
                        onClick = {
                            scope.launch {
                                try {
                                    log("Revoking token...")
                                    FirebaseMessaging.getInstance().deleteToken()
                                    tokenText = "Token deleted successfully."
                                    log("Success: Token revoked locally.")
                                } catch (e: Exception) {
                                    log("Failed: ${e.message}")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Delete FCM Token")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    IenText("Android Direct Boot Capability Status", style = IenTheme.typography.title3)
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ContinuousRoundedRectangle(8.dp))
                            .background(IenTheme.colors.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        val isDbSupported = remember { FirebaseMessagingDirectBoot.getInstance().isSupported }
                        IenText(
                            text = "Direct Boot Supported: ${if (isDbSupported) "🟢 ENABLED (Android Only)" else "🔴 UNAVAILABLE (iOS/No-op)"}",
                            style = IenTheme.typography.body2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        IenText(
                            text = "Direct Boot Aware components allow device protected storage access prior to user decryption.",
                            style = IenTheme.typography.label2,
                            color = IenTheme.colors.textSecondary.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    IenText("Topic Operations", style = IenTheme.typography.title3)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        log("Subscribing to topic 'news'...")
                                        FirebaseMessaging.getInstance().subscribeToTopic("news")
                                        log("Success: Subscribed to 'news'")
                                    } catch (e: Exception) {
                                        log("Failed: ${e.message}")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Sub 'news'")
                        }

                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        log("Unsubscribing from 'news'...")
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("news")
                                        log("Success: Unsubscribed from 'news'")
                                    } catch (e: Exception) {
                                        log("Failed: ${e.message}")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Unsub 'news'")
                        }
                    }

                    // Logs Output
                    IenText("Event Logs", style = IenTheme.typography.title3)
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
                            IenText("Console ready.", color = Color.Gray, style = IenTheme.typography.body2)
                        } else {
                            logs.forEach { logLine ->
                                IenText("> $logLine", color = IenTheme.colors.brand, style = IenTheme.typography.body2)
                            }
                        }
                    }
                }
            }
        }
    }
}
