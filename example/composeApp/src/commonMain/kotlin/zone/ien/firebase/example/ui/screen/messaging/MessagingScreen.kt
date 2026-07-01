package zone.ien.firebase.example.ui.screen.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import androidx.compose.runtime.saveable.rememberSaveable
import zone.ien.firebase.messaging.FirebasePush
import zone.ien.firebase.messaging.NotificationContent
import zone.ien.firebase.messaging.NotificationFormatter
import zone.ien.firebase.messaging.PushListener
import zone.ien.firebase.messaging.PushDisplayMode
import zone.ien.utils.utils.Dlog
import zone.ien.utils.utils.toClipEntry

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MessagingScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val clipboard = LocalClipboard.current

    val initError = remember {
        if (!FirebaseApp.isInitialized) {
            "Firebase Core must be initialized first. Go to 'Firebase Init' screen."
        } else {
            runCatching { FirebasePush.notifier }.exceptionOrNull()?.message
        }
    }

    var logText by rememberSaveable { mutableStateOf("Ready to inspect Firebase Cloud Messaging.") }
    var tokenValue by rememberSaveable { mutableStateOf("-") }
    var displayMode by rememberSaveable { mutableStateOf(PushDisplayMode.AUTO_DISPLAY) }

    LaunchedEffect(displayMode) {
        if (initError == null) {
            runCatching {
                FirebasePush.displayMode = displayMode
            }
        }
    }

    LaunchedEffect(initError) {
        if (initError == null) {
            runCatching {
                // Initialize push configuration including notification formatter
                zone.ien.firebase.example.KmpPushInitializer.initialize(formatter = zone.ien.firebase.example.ExampleNotificationFormatter())

                // Register unified FirebasePush listener matching reference architecture
                FirebasePush.setListener(object : PushListener {
                    override fun onNewToken(token: String) {
                        tokenValue = token
                        logText = "FCM registration token updated:\n$token"
                    }

                    override fun onPayloadData(data: Map<String, String>) {
                        val prettyPayload = data.entries.joinToString(separator = "\n") { "${it.key}: ${it.value}" }
                        logText = "New Data Payload Received:\n\n$prettyPayload"
                    }

                    override fun onPushNotification(title: String?, body: String?) {
                        logText = "New Push Notification Received:\nTitle: $title\nBody: $body"
                    }

                    override fun onPushNotificationWithPayloadData(title: String?, body: String?, data: Map<String, String>) {
                        val prettyPayload = data.entries.joinToString(separator = "\n") { "${it.key}: ${it.value}" }
                        logText = "New Notification & Data Received:\nTitle: $title\nBody: $body\n\nPayload:\n$prettyPayload"
                    }
                })
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cloud Messaging Demo") },
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
            if (initError != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = initError,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Text(
                    text = "Observe real-time FCM tokens and received background/foreground push notification payloads.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    clipboard.setClipEntry(tokenValue.toClipEntry())
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Text("Current FCM Token:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(
                            text = tokenValue,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                logText = "Retrieving FCM device token..."
                                try {
                                    val token = FirebasePush.notifier.getToken() ?: "-"
                                    tokenValue = token
                                    logText = "Token retrieved successfully!\nToken: $token"
                                } catch (e: Exception) {
                                    logText = "Failed to retrieve token:\n${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Get Token")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                logText = "Deleting FCM device token..."
                                try {
                                    FirebasePush.notifier.deleteMyToken()
                                    tokenValue = "-"
                                    logText = "Token deleted successfully!"
                                } catch (e: Exception) {
                                    logText = "Failed to delete token:\n${e.message}"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete Token")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()

                Text(
                    text = "Configure Push Display Mode",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = displayMode == PushDisplayMode.AUTO_DISPLAY,
                            onClick = { displayMode = PushDisplayMode.AUTO_DISPLAY }
                        )
                        Text(
                            text = "AUTO_DISPLAY\n(SDK displays local notification popup)",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { displayMode = PushDisplayMode.AUTO_DISPLAY }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = displayMode == PushDisplayMode.CALLBACK_ONLY,
                            onClick = { displayMode = PushDisplayMode.CALLBACK_ONLY }
                        )
                        Text(
                            text = "CALLBACK_ONLY\n(Silence banner, app listener handling only)",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { displayMode = PushDisplayMode.CALLBACK_ONLY }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()

                Text(
                    text = "Event Terminal Logs",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = logText,
                            color = Color.Green,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
