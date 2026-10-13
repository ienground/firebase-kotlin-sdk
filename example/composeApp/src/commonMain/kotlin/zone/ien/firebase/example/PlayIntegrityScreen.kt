package zone.ien.firebase.example
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

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.firebase.appcheck.FirebaseAppCheck
import zone.ien.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

@Composable
public fun PlayIntegrityScreen(onBack: () -> Unit) {
    val defaultColor = IenTheme.colors.textSecondary
    val primaryColor = IenTheme.colors.brand
    val errorColor = IenTheme.colors.danger

    var statusText by remember { mutableStateOf("Idle") }
    var statusColor by remember { mutableStateOf(defaultColor) }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Play Integrity Provider") },
                navigationIcon = { IenBackButton(onClick = onBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Android Play Integrity App Check",
                        style = IenTheme.typography.title2,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "This provider uses Google Play Integrity API to verify the authenticity of your app. This is an Android-exclusive feature.",
                        style = IenTheme.typography.body1
                    )
                }
            }

            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "⚠️ Setup Prerequisites",
                        style = IenTheme.typography.title3,
                        color = IenTheme.colors.danger
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "1. Enable Play Integrity API in Google Play Console.\n" +
                               "2. Register Play Integrity provider in Firebase Console App Check.\n" +
                               "3. Add your app's SHA-256 fingerprint in Firebase Console settings.",
                        style = IenTheme.typography.body2
                    )
                }
            }

            IenButton(
                onClick = {
                    try {
                        // 1. Get Play Integrity Provider Factory
                        val factory = PlayIntegrityAppCheckProviderFactory.getInstance()
                        // 2. Install to Firebase App Check
                        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(factory)
                        statusText = "Play Integrity Provider Installed Successfully!"
                        statusColor = primaryColor
                    } catch (e: UnsupportedOperationException) {
                        statusText = "Not Supported: ${e.message}"
                        statusColor = errorColor
                    } catch (e: Exception) {
                        statusText = "Error: ${e.message}"
                        statusColor = errorColor
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                IenText("Install Play Integrity Provider")
            }

            Spacer(modifier = Modifier.height(16.dp))

            IenText(
                text = "Status: $statusText",
                color = statusColor,
                style = IenTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
