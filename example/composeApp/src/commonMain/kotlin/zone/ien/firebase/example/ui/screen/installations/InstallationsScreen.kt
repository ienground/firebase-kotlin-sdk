package zone.ien.firebase.example.ui.screen.installations
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zone.ien.firebase.installations.FirebaseInstallations
import com.kyant.capsule.ContinuousRoundedRectangle

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

    val primaryColor = IenTheme.colors.brand
    val errorColor = IenTheme.colors.danger

    if (showDeleteConfirmDialog) {
        IenConfirmDialog(
            visible = showDeleteConfirmDialog,
            title = "⚠️ CAUTION: Delete Installation",
            message = "This action deletes the Firebase Installation ID and all associated data " +
                "from both this client device and the Firebase backend.\n\n" +
                "WARNING: All active Firebase App Tokens (e.g. FCM push tokens, Auth tokens) " +
                "will be immediately invalidated. Only proceed if you intend to perform an instance reset.",
            confirmText = "Delete Anyway",
            dismissText = "Cancel",
            destructive = true,
            onDismissRequest = { showDeleteConfirmDialog = false },
            onConfirmClick = {
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
            }
        )
    }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Firebase Installations", fontWeight = FontWeight.Bold) },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            IenTheme.colors.surface,
                            IenTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                )
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pre-requisites Alert Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                shape = ContinuousRoundedRectangle(16.dp),
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "ℹ️ Connectivity Requirements",
                        
                        fontWeight = FontWeight.Bold,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    IenText(
                        text = "Firebase Installations APIs require a valid google-services configuration, " +
                               "active network connection, and a initialized FirebaseApp instance.",
                        
                        color = IenTheme.colors.brand.copy(alpha = 0.8f)
                    )
                }
            }

            // Info Card displaying states
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenText(
                        text = "Installation Info",
                        
                        fontWeight = FontWeight.Bold
                    )

                    Column {
                        IenText("Installation ID:",  fontWeight = FontWeight.SemiBold, color = primaryColor)
                        IenText(installationId, )
                    }

                    IenDivider()

                    Column {
                        IenText("Auth Token:",  fontWeight = FontWeight.SemiBold, color = primaryColor)
                        IenText(authToken, )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            IenText("Expires At:",  fontWeight = FontWeight.SemiBold)
                            IenText(tokenExpiresAt, )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IenText("Created At:",  fontWeight = FontWeight.SemiBold)
                            IenText(tokenCreatedAt, )
                        }
                    }
                }
            }

            // API trigger actions Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                shape = ContinuousRoundedRectangle(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "API Actions",
                        
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    IenButton(
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
                        IenText("Get Installation ID")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IenText("Force Refresh Token:", )
                        IenSwitch(
                            checked = forceRefresh,
                            onCheckedChange = { forceRefresh = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    IenButton(
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
                        ) {
                        IenText("Get Auth Token")
                    }

                    IenDivider(modifier = Modifier.padding(vertical = 12.dp))

                    IenButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        ) {
                        IenText("Delete Installation Instance")
                    }
                }
            }

            // Console Log Output
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                shape = ContinuousRoundedRectangle(16.dp),
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Console Logs",
                        
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    IenText(
                        text = logMessage,
                        style = LocalTextStyle.current.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                        color = IenTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}
