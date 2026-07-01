package zone.ien.firebase.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.firebase.appcheck.FirebaseAppCheck
import zone.ien.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PlayIntegrityScreen(onBack: () -> Unit) {
    val defaultColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    var statusText by remember { mutableStateOf("Idle") }
    var statusColor by remember { mutableStateOf(defaultColor) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Play Integrity Provider") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Android Play Integrity App Check",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This provider uses Google Play Integrity API to verify the authenticity of your app. This is an Android-exclusive feature.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Setup Prerequisites",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Enable Play Integrity API in Google Play Console.\n" +
                               "2. Register Play Integrity provider in Firebase Console App Check.\n" +
                               "3. Add your app's SHA-256 fingerprint in Firebase Console settings.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
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
                Text("Install Play Integrity Provider")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Status: $statusText",
                color = statusColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
