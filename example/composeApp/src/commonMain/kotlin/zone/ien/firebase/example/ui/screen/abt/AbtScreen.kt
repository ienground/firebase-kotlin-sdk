package zone.ien.firebase.example.ui.screen.abt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zone.ien.firebase.abt.AbtException
import zone.ien.firebase.abt.FirebaseABTesting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbtScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var logMessage by remember { mutableStateOf("Ready to verify A/B Testing components.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("A/B Testing", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", fontSize = 20.sp)
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
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "KMP A/B Testing Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This screen validates the compiled binding classes and exceptions for the ':firebase-abt' KMP wrapper.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {
                    try {
                        logMessage = "Verifying KMP classes...\n"
                        val abtClass = FirebaseABTesting::class
                        val simpleName = abtClass.simpleName ?: "FirebaseABTesting (Native ObjC Class)"
                        logMessage += "FirebaseABTesting KClass resolved successfully!\n"
                        logMessage += "Simple Name: $simpleName\n"
                        logMessage += "Reflect qualifiedName on iOS: ${abtClass.qualifiedName ?: "null (Kotlin/Native Reflection Limitation)"}\n"
                    } catch (e: Exception) {
                        logMessage += "Verification Failed: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify SDK Classes")
            }

            Button(
                onClick = {
                    try {
                        throw AbtException("Test AbtException message")
                    } catch (e: AbtException) {
                        logMessage = "AbtException Successfully Caught!\nMessage: ${e.message}"
                    } catch (e: Exception) {
                        logMessage = "Caught generic exception: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Trigger and Catch AbtException")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Console Output Logs",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = logMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
