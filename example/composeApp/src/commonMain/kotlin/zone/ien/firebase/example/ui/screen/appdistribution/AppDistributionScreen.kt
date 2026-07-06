package zone.ien.firebase.example.ui.screen.appdistribution

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.appdistribution.AppDistributionRelease
import zone.ien.firebase.appdistribution.FirebaseAppDistribution
import zone.ien.firebase.appdistribution.UpdateProgress
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.example.util.isIos
import zone.ien.utils.ui.wrapper.M3RootWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDistributionScreen(
    context: FirebasePlatformContext,
    onBack: () -> Unit
) {
    val isSupported = true
    val isUpdateProgressSupported = !isIos
    val appDistribution = remember {
        if (isSupported) {
            runCatching { FirebaseAppDistribution.instance }.getOrNull()
        } else {
            null
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val logs = remember { 
        mutableStateListOf<String>().apply {
            if (isIos) {
                add("iOS Notice: In-app update progress monitoring is not supported. Check for updates will prompt the native SDK alert.")
            }
        }
    }

    var isTesterSignedIn by remember(appDistribution) { mutableStateOf(appDistribution?.isTesterSignedIn ?: false) }
    var latestRelease by remember { mutableStateOf<AppDistributionRelease?>(null) }
    var updateProgress by remember { mutableStateOf<UpdateProgress?>(null) }
    var isChecking by remember { mutableStateOf(false) }

    fun log(msg: String) {
        logs.add(msg)
    }

    M3RootWrapper {
        AppTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("App Distribution") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Text(
                                    text = "←",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
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
                    if (isIos) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "ℹ️ iOS Platform Notice",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "In-app update progress tracking is unsupported on iOS. Checking for releases will automatically prompt the native SDK update flow if a release is available.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = "Tester Authentication",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        log("Initiating tester sign in...")
                                        appDistribution?.signInTester()
                                        isTesterSignedIn = appDistribution?.isTesterSignedIn ?: false
                                        log("Tester signed in successfully: $isTesterSignedIn")
                                    } catch (e: Exception) {
                                        log("Sign in failed: ${e.message}")
                                    }
                                }
                            },
                            enabled = isSupported && !isTesterSignedIn,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign In Tester")
                        }

                        Button(
                            onClick = {
                                try {
                                    appDistribution?.signOutTester()
                                    isTesterSignedIn = appDistribution?.isTesterSignedIn ?: false
                                    log("Signed out tester successfully.")
                                } catch (e: Exception) {
                                    log("Sign out failed: ${e.message}")
                                }
                            },
                            enabled = isSupported && isTesterSignedIn,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign Out")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Prerelease Updates",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    isChecking = true
                                    log("Checking for updates...")
                                    val release = appDistribution?.checkForNewRelease()
                                    latestRelease = release
                                    if (release != null) {
                                        log("Latest release found: ${release.displayVersion} (${release.versionCode})")
                                    } else {
                                        log("No updates found.")
                                    }
                                } catch (e: Exception) {
                                    log("Check failed: ${e.message}")
                                } finally {
                                    isChecking = false
                                }
                            }
                        },
                        enabled = isSupported && !isChecking,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Check For New Release")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    log("Starting update...")
                                    appDistribution?.updateIfNewReleaseAvailable()?.collect { progress ->
                                        log("Update Progress: ${progress.apkBytesDownloaded} / ${progress.apkFileTotalBytes} (${progress.updateStatus})")
                                    }
                                    log("Update check finished.")
                                } catch (e: Exception) {
                                    log("Update failed: ${e.message}")
                                }
                            }
                        },
                        enabled = isSupported && latestRelease != null && isUpdateProgressSupported,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isIos) "Update App (Android Only)" else "Update App")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Execution Log",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (logs.isEmpty()) {
                                Text(
                                    "No events logged yet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            } else {
                                logs.forEach { msg ->
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
