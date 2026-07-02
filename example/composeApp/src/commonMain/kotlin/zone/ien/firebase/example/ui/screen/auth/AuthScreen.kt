package zone.ien.firebase.example.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.auth.FirebaseAuth
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.GoogleAuthProvider
import zone.ien.firebase.auth.GithubAuthProvider
import zone.ien.firebase.auth.OAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AuthScreen(onBack: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Real-time auth state updates via authStateFlow
    val currentUserState = auth.authStateFlow.collectAsState(initial = auth.currentUser)
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

    val defaultColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    var statusColor by remember { mutableStateOf(defaultColor) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firebase Authentication") },
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ Configuration Pre-requisites",
                        style = MaterialTheme.typography.titleMedium,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Enable 'Anonymous' and 'Email/Password' providers in the Firebase Console.\n" +
                               "2. Configure social login keys (Google, GitHub, Apple) under Console > Authentication > Sign-in method.\n" +
                               "3. [iOS] Verify Bundle ID and custom URL schemes match configurations when using native OAuth client.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Authentication Status Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Current Session Info", style = MaterialTheme.typography.titleSmall)
                    if (user != null) {
                        Text("UID: ${user.uid}", style = MaterialTheme.typography.bodyMedium)
                        Text("Email: ${user.email ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text("Anonymous: ${user.isAnonymous}", style = MaterialTheme.typography.bodyMedium)
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
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
                                Text("Get ID Token")
                            }

                            Button(
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
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Delete User")
                            }
                        }
                    } else {
                        Text("Signed Out / No Active Session", style = MaterialTheme.typography.bodyMedium, color = errorColor)
                    }
                }
            }

            // Email & Password Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Email & Password Authentication", style = MaterialTheme.typography.titleSmall)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
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
                            Text("Sign In")
                        }
                        Button(
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
                            Text("Register")
                        }
                    }
                }
            }

            // Credential & Social/OAuth Authentication Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Social & Custom Credential Sign In", style = MaterialTheme.typography.titleSmall)
                    
                    OutlinedTextField(
                        value = tokenField,
                        onValueChange = { tokenField = it },
                        label = { Text("General Token (GitHub)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = idTokenField,
                        onValueChange = { idTokenField = it },
                        label = { Text("ID Token (Google / Apple)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = accessTokenField,
                        onValueChange = { accessTokenField = it },
                        label = { Text("Access Token (Google / Custom)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rawNonceField,
                        onValueChange = { rawNonceField = it },
                        label = { Text("Raw Nonce (Apple / OIDC)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Select Provider to Sign In", style = MaterialTheme.typography.bodyMedium, color = primaryColor)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
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
                            Text("Google")
                        }

                        Button(
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
                            Text("GitHub")
                        }

                        Button(
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
                            Text("Apple")
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Custom OAuth Provider", style = MaterialTheme.typography.bodyMedium, color = primaryColor)

                    OutlinedTextField(
                        value = providerId,
                        onValueChange = { providerId = it },
                        label = { Text("Custom Provider ID (e.g. microsoft.com)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
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
                        Text("Sign In with Custom OAuth")
                    }
                }
            }

            // Anonymous & Sign Out Actions Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Quick Access & Session Control", style = MaterialTheme.typography.titleSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
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
                            Text("Anonymous Sign In")
                        }
                        Button(
                            onClick = {
                                auth.signOut()
                                idTokenText = ""
                                statusText = "Signed out successfully."
                                statusColor = primaryColor
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Sign Out")
                        }
                    }
                }
            }

            if (idTokenText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Session ID Token", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = idTokenText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: $statusText",
                color = statusColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
