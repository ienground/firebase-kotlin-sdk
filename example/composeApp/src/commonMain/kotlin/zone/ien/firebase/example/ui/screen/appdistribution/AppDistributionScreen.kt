package zone.ien.firebase.example.ui.screen.appdistribution
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
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.appdistribution.AppDistributionRelease
import zone.ien.firebase.appdistribution.FirebaseAppDistribution
import zone.ien.firebase.appdistribution.UpdateProgress
import zone.ien.firebase.example.ui.theme.AppTheme
import zone.ien.firebase.example.util.isIos
import zone.ien.utils.ui.wrapper.IenRootWrapper
import com.kyant.capsule.ContinuousRoundedRectangle

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

    IenRootWrapper {
        AppTheme {
            IenScaffold(
                topBar = {
                    IenTopAppBar(
                        title = { IenText("App Distribution") },
                        navigationIcon = { IenBackButton(onClick = onBack) })
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
                                .clip(ContinuousRoundedRectangle(8.dp))
                                .background(IenTheme.colors.brandWeak)
                                .padding(12.dp)
                        ) {
                            IenText(
                                text = "ℹ️ iOS Platform Notice",
                                style = IenTheme.typography.title2,
                                color = IenTheme.colors.brand
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            IenText(
                                text = "In-app update progress tracking is unsupported on iOS. Checking for releases will automatically prompt the native SDK update flow if a release is available.",
                                style = IenTheme.typography.body2,
                                color = IenTheme.colors.brand
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    IenText(
                        text = "Tester Authentication",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IenButton(
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
                            state = IenButtonState(enabled = isSupported && !isTesterSignedIn),
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Sign In Tester")
                        }

                        IenButton(
                            onClick = {
                                try {
                                    appDistribution?.signOutTester()
                                    isTesterSignedIn = appDistribution?.isTesterSignedIn ?: false
                                    log("Signed out tester successfully.")
                                } catch (e: Exception) {
                                    log("Sign out failed: ${e.message}")
                                }
                            },
                            state = IenButtonState(enabled = isSupported && isTesterSignedIn),
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Sign Out")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IenText(
                        text = "Prerelease Updates",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray
                    )

                    IenButton(
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
                        state = IenButtonState(enabled = isSupported && !isChecking),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Check For New Release")
                    }

                    IenButton(
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
                        state = IenButtonState(enabled = isSupported && latestRelease != null && isUpdateProgressSupported),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText(if (isIos) "Update App (Android Only)" else "Update App")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    IenText(
                        text = "Execution Log",
                        style = IenTheme.typography.title2
                    )

                    IenSurface(color = IenTheme.colors.surfaceVariant, 
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (logs.isEmpty()) {
                                IenText(
                                    "No events logged yet.",
                                    style = IenTheme.typography.body2,
                                    color = Color.Gray
                                )
                            } else {
                                logs.forEach { msg ->
                                    IenText(
                                        text = msg,
                                        style = IenTheme.typography.body2,
                                        color = IenTheme.colors.textSecondary
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
