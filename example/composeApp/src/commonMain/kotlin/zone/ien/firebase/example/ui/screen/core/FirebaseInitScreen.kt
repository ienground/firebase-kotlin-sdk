package zone.ien.firebase.example.ui.screen.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.FirebasePlatformContext
import zone.ien.firebase.example.data.AppStateManager
import zone.ien.firebase.example.data.FirebaseInitState
import zone.ien.firebase.example.ui.toast.showToast

@Composable
fun FirebaseInitScreen(context: FirebasePlatformContext, onBack: () -> Unit) {
    val isInitialized = AppStateManager.initState == FirebaseInitState.Initialized
    val isInitializing = AppStateManager.initState == FirebaseInitState.Initializing
    
    var statusText by remember(AppStateManager.initState) {
        mutableStateOf(
            when (AppStateManager.initState) {
                FirebaseInitState.NotInitialized -> "Firebase Not Initialized"
                FirebaseInitState.Initializing -> "Firebase Initializing..."
                FirebaseInitState.Initialized -> "Firebase Initialized"
                FirebaseInitState.InitializationFailed -> "Initialization Failed"
            }
        )
    }
    var detailMessage by remember { mutableStateOf("Ready to trigger initialization check.") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Firebase Core Init",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (AppStateManager.initState) {
                        FirebaseInitState.Initialized -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        FirebaseInitState.Initializing -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                        FirebaseInitState.InitializationFailed -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                        FirebaseInitState.NotInitialized -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Core Setup Status",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = when (AppStateManager.initState) {
                            FirebaseInitState.Initialized -> if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32)
                            FirebaseInitState.Initializing -> MaterialTheme.colorScheme.primary
                            FirebaseInitState.InitializationFailed -> MaterialTheme.colorScheme.error
                            FirebaseInitState.NotInitialized -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = detailMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isInitialized) {
                        showToast("Firebase is already initialized.")
                        return@Button
                    }
                    if (isInitializing) {
                        showToast("Firebase initialization is in progress.")
                        return@Button
                    }
                    scope.launch {
                        try {
                            AppStateManager.initState = FirebaseInitState.Initializing
                            FirebaseApp.initialize(context)
                            AppStateManager.initState = FirebaseInitState.Initialized
                            val wellKnownValid = zone.ien.firebase.example.ui.test.WellKnownTypesTest.verifyCompilation()
                            val sessionsValid = zone.ien.firebase.example.ui.test.SessionsTest.verifyCompilation()
                            statusText = "Firebase Initialized Successfully!"
                            detailMessage = "App: ${FirebaseApp.instance.getName()} (WellKnownTypes: $wellKnownValid, Sessions: $sessionsValid)"
                            showToast("Firebase initialized successfully.")
                        } catch (e: Exception) {
                            AppStateManager.initState = FirebaseInitState.InitializationFailed
                            statusText = "Initialization Failed"
                            detailMessage = e.message ?: "Unknown initial configuration error occurred."
                            showToast("Initialization failed: ${e.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isInitializing && !isInitialized,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInitialized) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when {
                        isInitializing -> "Initializing..."
                        isInitialized -> "Initialized (Double Tap Prevented)"
                        else -> "Initialize Firebase"
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Platform File Rules Description
            Text(
                text = "Platform Configuration Instructions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Android:\nMust place 'google-services.json' in 'example/androidApp/' directory. Initialization is constrained to run only in the Main Process to prevent secondary process memory crashes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "iOS:\nMust place 'GoogleService-Info.plist' inside 'example/iosApp/iosApp/' directory and register it as an Xcode project bundle resource.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
