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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.appdistribution.AppDistributionRelease
import zone.ien.firebase.appdistribution.FirebaseAppDistribution
import zone.ien.firebase.appdistribution.UpdateProgress
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.utils.ui.wrapper.M3RootWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDistributionScreen(
    context: FirebasePlatformContext,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val appDistribution = remember { FirebaseAppDistribution.instance }
    val logs = remember { mutableStateListOf<String>() }

    var isTesterSignedIn by remember { mutableStateOf(appDistribution.isTesterSignedIn) }
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
                    Text(
                        text = "Tester Authentication",
                        style = MaterialTheme.typography.titleMedium
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
                                        appDistribution.signInTester()
                                        isTesterSignedIn = appDistribution.isTesterSignedIn
                                        log("Tester signed in successfully: $isTesterSignedIn")
                                    } catch (e: Exception) {
                                        log("Sign in failed: ${e.message}")
                                    }
                                }
                            },
                            enabled = !isTesterSignedIn,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign In Tester")
                        }

                        Button(
                            onClick = {
                                try {
                                    log("Signing out tester...")
                                    appDistribution.signOutTester()
                                    isTesterSignedIn = appDistribution.isTesterSignedIn
                                    log("Tester signed out. Status: $isTesterSignedIn")
                                } catch (e: Exception) {
                                    log("Sign out failed: ${e.message}")
                                }
                            },
                            enabled = isTesterSignedIn,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign Out")
                        }
                    }

                    Text(
                        text = "Tester Signed In Status: $isTesterSignedIn",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isTesterSignedIn) Color(0xFF2E7D32) else Color.Red
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Prerelease Build Management",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    isChecking = true
                                    log("Checking for new release...")
                                    val release = appDistribution.checkForNewRelease()
                                    latestRelease = release
                                    if (release != null) {
                                        log("Release Available! version: ${release.displayVersion} (${release.versionCode})")
                                    } else {
                                        log("No new release available.")
                                    }
                                } catch (e: Exception) {
                                    log("Check for release failed: ${e.message}")
                                } finally {
                                    isChecking = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isChecking) "Checking..." else "Check for Release")
                    }

                    latestRelease?.let { release ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Version: ${release.displayVersion}", style = MaterialTheme.typography.bodyMedium)
                            Text("Version Code: ${release.versionCode}", style = MaterialTheme.typography.bodyMedium)
                            Text("Release Notes: ${release.releaseNotes ?: "None"}", style = MaterialTheme.typography.bodyMedium)
                            Text("Binary Type: ${release.binaryType}", style = MaterialTheme.typography.bodyMedium)
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    log("Starting in-app update task flow...")
                                    appDistribution.updateIfNewReleaseAvailable()
                                        .catch { error ->
                                            log("In-app update failed: ${error.message}")
                                        }
                                        .collect { progress ->
                                            updateProgress = progress
                                            log("Progress: ${progress.updateStatus} (${progress.apkBytesDownloaded}/${progress.apkFileTotalBytes} bytes)")
                                        }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Start Update")
                        }
                    }

                    updateProgress?.let { progress ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Status: ${progress.updateStatus.name}", style = MaterialTheme.typography.bodyMedium)
                            if (progress.apkFileTotalBytes > 0) {
                                val ratio = progress.apkBytesDownloaded.toFloat() / progress.apkFileTotalBytes.toFloat()
                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text("${(ratio * 100).toInt()}% downloaded", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Text(
                        text = "Verification Output Log",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (logs.isEmpty()) {
                            Text("No actions performed yet.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        } else {
                            logs.forEach { logLine ->
                                Text("> $logLine", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Button(
                        onClick = { logs.clear() },
                        colors = ButtonDefaults.textButtonColors(),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Clear Log")
                    }

                    Text(
                        text = "* Note: App Distribution in-app updates require the app to be registered in Firebase, tester account invited, and run on a physical device/simulator linked to the Firebase app project.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
