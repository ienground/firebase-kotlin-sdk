package zone.ien.firebase.example.ui.screen.messaging

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import zone.ien.firebase.messaging.FirebaseMessaging
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.ui.wrapper.M3RootWrapper

@OptIn(ExperimentalMaterial3Api::class)
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

    M3RootWrapper {
        AppTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Firebase Messaging") },
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
                        text = "FCM Client SDK Verification Panel",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "This screen acts as a developer utility to request and revoke registration tokens or manage topic subscriptions using the KMP wrappers.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Display Current Token Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text("Current Token:", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tokenText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Token Actions
                    Button(
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
                        Text("Get FCM Token")
                    }

                    Button(
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
                        Text("Delete FCM Token")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Topic Operations", style = MaterialTheme.typography.titleSmall)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
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
                            Text("Sub 'news'")
                        }

                        Button(
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
                            Text("Unsub 'news'")
                        }
                    }

                    // Logs Output
                    Text("Event Logs", style = MaterialTheme.typography.titleSmall)
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
                            Text("Console ready.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        } else {
                            logs.forEach { logLine ->
                                Text("> $logLine", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
