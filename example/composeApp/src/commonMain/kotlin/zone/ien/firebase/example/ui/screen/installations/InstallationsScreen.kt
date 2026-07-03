package zone.ien.firebase.example.ui.screen.installations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zone.ien.firebase.installations.FirebaseInstallations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallationsScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var installationId by remember { mutableStateOf("Not fetched yet") }
    var authToken by remember { mutableStateOf("Not fetched yet") }
    var tokenExpiresAt by remember { mutableStateOf("N/A") }
    var tokenCreatedAt by remember { mutableStateOf("N/A") }
    var forceRefresh by remember { mutableStateOf(false) }

    var logMessage by remember { mutableStateOf("Ready to query Firebase Installations API.") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("⚠️ CAUTION: Delete Installation", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This action deletes the Firebase Installation ID and all associated data " +
                    "from both this client device and the Firebase backend.\n\n" +
                    "WARNING: All active Firebase App Tokens (e.g. FCM push tokens, Auth tokens) " +
                    "will be immediately invalidated. Only proceed if you intend to perform an instance reset."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        coroutineScope.launch {
                            try {
                                logMessage = "Initiating delete operation..."
                                FirebaseInstallations.instance.delete()
                                logMessage = "Installation deleted successfully! Instance ID has been reset."
                                installationId = "Deleted"
                                authToken = "Deleted"
                                tokenExpiresAt = "N/A"
                                tokenCreatedAt = "N/A"
                            } catch (e: Exception) {
                                logMessage = "Delete Failed: ${e.message}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = errorColor)
                ) {
                    Text("Delete Anyway")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firebase Installations", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pre-requisites Alert Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ Connectivity Requirements",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Firebase Installations APIs require a valid google-services configuration, " +
                               "active network connection, and a initialized FirebaseApp instance.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            // Info Card displaying states
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Installation Info",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column {
                        Text("Installation ID:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
                        Text(installationId, fontSize = 14.sp)
                    }

                    HorizontalDivider()

                    Column {
                        Text("Auth Token:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
                        Text(authToken, fontSize = 14.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Expires At:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(tokenExpiresAt, fontSize = 13.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Created At:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(tokenCreatedAt, fontSize = 13.sp)
                        }
                    }
                }
            }

            // API trigger actions Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "API Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    logMessage = "Fetching installation ID..."
                                    val id = FirebaseInstallations.instance.getId()
                                    installationId = id
                                    logMessage = "Success fetching installation ID: $id"
                                } catch (e: Exception) {
                                    logMessage = "ID Query Failed: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Get Installation ID")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Force Refresh Token:", fontSize = 14.sp)
                        Switch(
                            checked = forceRefresh,
                            onCheckedChange = { forceRefresh = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    logMessage = "Fetching installation Auth Token..."
                                    val result = FirebaseInstallations.instance.getToken(forceRefresh)
                                    authToken = result.token
                                    tokenExpiresAt = "${result.tokenExpirationTimestamp} ms"
                                    tokenCreatedAt = "${result.tokenCreationTimestamp} ms"
                                    logMessage = "Success fetching Auth Token (Expires: ${result.tokenExpirationTimestamp} ms)"
                                } catch (e: Exception) {
                                    logMessage = "Token Query Failed: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Get Auth Token")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Button(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = errorColor)
                    ) {
                        Text("Delete Installation Instance")
                    }
                }
            }

            // Console Log Output
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Console Logs",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = logMessage,
                        fontSize = 13.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
