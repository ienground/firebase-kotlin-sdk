package zone.ien.firebase.example.ui.screen.core
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.kyant.capsule.ContinuousRoundedRectangle

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

    IenScaffold(
        topBar = {
            IenTopAppBar(
                navigationIcon = { IenBackButton(onClick = onBack) },
                title = {
                    IenText(
                        text = "Firebase Core Init",
                        style = IenTheme.typography.title1,
                        color = IenTheme.colors.textPrimary
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
            IenSurface(
                color = IenTheme.colors.surfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                shape = ContinuousRoundedRectangle(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    IenText(
                        text = "Core Setup Status",
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    IenText(
                        text = statusText,
                        style = IenTheme.typography.title1,
                        color = when (AppStateManager.initState) {
                            FirebaseInitState.Initialized -> if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32)
                            FirebaseInitState.Initializing -> IenTheme.colors.brand
                            FirebaseInitState.InitializationFailed -> IenTheme.colors.danger
                            FirebaseInitState.NotInitialized -> IenTheme.colors.textSecondary
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    IenText(
                        text = detailMessage,
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textPrimary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            IenButton(
                onClick = {
                    if (isInitialized) {
                        showToast("Firebase is already initialized.")
                        return@IenButton
                    }
                    if (isInitializing) {
                        showToast("Firebase initialization is in progress.")
                        return@IenButton
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
                state = IenButtonState(enabled = !isInitializing && !isInitialized),

            ) {
                IenText(
                    text = when {
                        isInitializing -> "Initializing..."
                        isInitialized -> "Initialized (Double Tap Prevented)"
                        else -> "Initialize Firebase"
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Platform File Rules Description
            IenText(
                text = "Platform Configuration Instructions",
                style = IenTheme.typography.title2,
                color = IenTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth(),
                
                shape = ContinuousRoundedRectangle(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "Android:\nMust place 'google-services.json' in 'example/androidApp/' directory. Initialization is constrained to run only in the Main Process to prevent secondary process memory crashes.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textPrimary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    IenText(
                        text = "iOS:\nMust place 'GoogleService-Info.plist' inside 'example/iosApp/iosApp/' directory and register it as an Xcode project bundle resource.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textPrimary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
