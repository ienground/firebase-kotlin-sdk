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
import zone.ien.firebase.messaging.FirebaseMessaging
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
            runCatching { FirebaseMessaging.getInstance() }.exceptionOrNull()?.message
        }
    }

    var logText by remember { mutableStateOf("Ready to inspect Firebase Cloud Messaging.") }
    var tokenValue by remember { mutableStateOf("-") }

    LaunchedEffect(initError) {
        if (initError == null) {
            runCatching {
                FirebaseMessaging.getInstance().addOnMessageReceivedListener { data ->
                    val prettyPayload = data.entries.joinToString(separator = "\n") { "${it.key}: ${it.value}" }
                    logText = "New Data Payload Received:\n\n$prettyPayload"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cloud Messaging Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (initError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = initError,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Button(onClick = onBack) {
                        Text("Go Back")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "FCM Specification",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                        Text(
                            text = "Firebase Cloud Messaging (FCM) handles remote push notifications. Request or clear your device token for server-side push targeting.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
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
                                    val messaging = FirebaseMessaging.getInstance()
                                    val token = messaging.getToken()
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
                                    val messaging = FirebaseMessaging.getInstance()
                                    messaging.deleteToken()
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

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Console Output Log",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = logText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                    }
                }
            }
        }
    }
}
