package zone.ien.firebase.example.ui.screen.auth

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.auth.FirebaseAuth
import zone.ien.firebase.auth.GithubAuthProvider
import zone.ien.firebase.auth.GoogleAuthProvider
import zone.ien.firebase.auth.OAuthProvider
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldLabelOption
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar

@Composable
public fun AuthScreen(onBack: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Real-time auth state updates via authStateFlow
    val authStateFlow = remember(auth) { auth.authStateFlow }
    val currentUserState = authStateFlow.collectAsState(initial = auth.currentUser)
    val user = currentUserState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Credential Sign In inputs
    var providerId by remember { mutableStateOf("google.com") }
    var tokenField by remember { mutableStateOf("") }
    var idTokenField by remember { mutableStateOf("") }
    var accessTokenField by remember { mutableStateOf("") }
    var rawNonceField by remember { mutableStateOf("") }

    var statusText by remember { mutableStateOf("Idle") }
    var idTokenText by remember { mutableStateOf("") }

    val defaultColor = IenTheme.colors.textSecondary
    val primaryColor = IenTheme.colors.brand
    val errorColor = IenTheme.colors.danger

    var statusColor by remember { mutableStateOf(defaultColor) }

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Firebase Authentication") },
                navigationIcon = { IenBackButton(onClick = onBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        text = "ℹ️ Configuration Pre-requisites",
                        style = IenTheme.typography.title2,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        text = "1. Enable 'Anonymous' and 'Email/Password' providers in the Firebase Console.\n" +
                               "2. Configure social login keys (Google, GitHub, Apple) under Console > Authentication > Sign-in method.\n" +
                               "3. [iOS] Verify Bundle ID and custom URL schemes match configurations when using native OAuth client.",
                        style = IenTheme.typography.body2
                    )
                }
            }

            // Authentication Status Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Current Session Info", style = IenTheme.typography.title3)
                    if (user != null) {
                        IenText("UID: ${user.uid}", style = IenTheme.typography.body1)
                        IenText("Email: ${user.email ?: "N/A"}", style = IenTheme.typography.body1)
                        IenText("Anonymous: ${user.isAnonymous}", style = IenTheme.typography.body1)
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IenButton(
                                onClick = {
                                    scope.launch {
                                        try {
                                            statusText = "Fetching ID Token..."
                                            statusColor = primaryColor
                                            val token = user.getIdToken(forceRefresh = true)
                                            idTokenText = token
                                            statusText = "Token fetch completed!"
                                        } catch (e: Exception) {
                                            statusText = "Token Fetch Error: ${e.message}"
                                            statusColor = errorColor
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                IenText("Get ID Token")
                            }

                            IenButton(
                                onClick = {
                                    scope.launch {
                                        try {
                                            statusText = "Deleting User Profile..."
                                            statusColor = primaryColor
                                            user.delete()
                                            idTokenText = ""
                                            statusText = "User deleted successfully."
                                        } catch (e: Exception) {
                                            statusText = "Delete User Error: ${e.message}"
                                            statusColor = errorColor
                                        }
                                    }
                                },
                                
                                modifier = Modifier.weight(1f)
                            ) {
                                IenText("Delete User")
                            }
                        }
                    } else {
                        IenText("Signed Out / No Active Session", style = IenTheme.typography.body1, color = errorColor)
                    }
                }
            }

            // Email & Password Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Email & Password Authentication", style = IenTheme.typography.title3)
                    IenTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Signing in..."
                                        statusColor = primaryColor
                                        val result = auth.signInWithEmailAndPassword(email, password)
                                        statusText = "Welcome: ${result.user?.email}"
                                    } catch (e: Exception) {
                                        statusText = "Sign In Failed: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Sign In")
                        }
                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Creating Account..."
                                        statusColor = primaryColor
                                        val result = auth.createUserWithEmailAndPassword(email, password)
                                        statusText = "Account Created: ${result.user?.email}"
                                    } catch (e: Exception) {
                                        statusText = "Sign Up Failed: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Register")
                        }
                    }
                }
            }

            // Credential & Social/OAuth Authentication Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Social & Custom Credential Sign In", style = IenTheme.typography.title3)
                    
                    IenTextField(
                        value = tokenField,
                        onValueChange = { tokenField = it },
                        label = "General Token (GitHub)",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenTextField(
                        value = idTokenField,
                        onValueChange = { idTokenField = it },
                        label = "ID Token (Google / Apple)",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenTextField(
                        value = accessTokenField,
                        onValueChange = { accessTokenField = it },
                        label = "Access Token (Google / Custom)",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IenTextField(
                        value = rawNonceField,
                        onValueChange = { rawNonceField = it },
                        label = "Raw Nonce (Apple / OIDC)",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    IenText("Select Provider to Sign In", style = IenTheme.typography.body1, color = primaryColor)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Signing in with Google..."
                                        statusColor = primaryColor
                                        val cred = GoogleAuthProvider.getCredential(
                                            idToken = idTokenField.takeIf { it.isNotEmpty() },
                                            accessToken = accessTokenField.takeIf { it.isNotEmpty() }
                                        )
                                        val result = auth.signInWithCredential(cred)
                                        statusText = "Success (Google): ${result.user?.uid}"
                                    } catch (e: Exception) {
                                        statusText = "Google Fail: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Google")
                        }

                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Signing in with GitHub..."
                                        statusColor = primaryColor
                                        val cred = GithubAuthProvider.getCredential(tokenField)
                                        val result = auth.signInWithCredential(cred)
                                        statusText = "Success (GitHub): ${result.user?.uid}"
                                    } catch (e: Exception) {
                                        statusText = "GitHub Fail: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("GitHub")
                        }

                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Signing in with Apple..."
                                        statusColor = primaryColor
                                        val provider = OAuthProvider("apple.com")
                                        val cred = provider.getCredential(
                                            idToken = idTokenField.takeIf { it.isNotEmpty() },
                                            rawNonce = rawNonceField.takeIf { it.isNotEmpty() },
                                            accessToken = accessTokenField.takeIf { it.isNotEmpty() }
                                        )
                                        val result = auth.signInWithCredential(cred)
                                        statusText = "Success (Apple): ${result.user?.uid}"
                                    } catch (e: Exception) {
                                        statusText = "Apple Fail: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Apple")
                        }
                    }

                    IenDivider(modifier = Modifier.padding(vertical = 8.dp))
                    IenText("Custom OAuth Provider", style = IenTheme.typography.body1, color = primaryColor)

                    IenTextField(
                        value = providerId,
                        onValueChange = { providerId = it },
                        label = "Custom Provider ID (e.g. microsoft.com)",
                        labelOption = IenTextFieldLabelOption.Sustain,
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenButton(
                        onClick = {
                            scope.launch {
                                try {
                                    statusText = "Signing in with Custom OAuth..."
                                    statusColor = primaryColor
                                    val provider = OAuthProvider(providerId)
                                    val cred = provider.getCredential(
                                        idToken = idTokenField.takeIf { it.isNotEmpty() },
                                        rawNonce = rawNonceField.takeIf { it.isNotEmpty() },
                                        accessToken = accessTokenField.takeIf { it.isNotEmpty() }
                                    )
                                    val result = auth.signInWithCredential(cred)
                                    statusText = "Success (Custom): ${result.user?.uid}"
                                } catch (e: Exception) {
                                    statusText = "Custom OAuth Fail: ${e.message}"
                                    statusColor = errorColor
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenText("Sign In with Custom OAuth")
                    }
                }
            }

            // Anonymous & Sign Out Actions Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IenText("Quick Access & Session Control", style = IenTheme.typography.title3)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IenButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        statusText = "Authenticating Anonymously..."
                                        statusColor = primaryColor
                                        val result = auth.signInAnonymously()
                                        statusText = "Anonymous session: ${result.user?.uid}"
                                    } catch (e: Exception) {
                                        statusText = "Anonymous Fail: ${e.message}"
                                        statusColor = errorColor
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Anonymous Sign In")
                        }
                        IenButton(
                            onClick = {
                                auth.signOut()
                                idTokenText = ""
                                statusText = "Signed out successfully."
                                statusColor = primaryColor
                            },
                            
                            modifier = Modifier.weight(1f)
                        ) {
                            IenText("Sign Out")
                        }
                    }
                }
            }

            if (idTokenText.isNotEmpty()) {
                IenSurface(color = IenTheme.colors.surfaceVariant, 
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        IenText("Session ID Token", style = IenTheme.typography.title3)
                        Spacer(modifier = Modifier.height(4.dp))
                        IenText(
                            text = idTokenText,
                            style = IenTheme.typography.body2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            IenText(
                text = "Status: $statusText",
                color = statusColor,
                style = IenTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
