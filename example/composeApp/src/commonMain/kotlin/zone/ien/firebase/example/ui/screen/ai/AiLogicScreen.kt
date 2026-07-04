package zone.ien.firebase.example.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.ai.FirebaseAI
import zone.ien.firebase.ai.ai

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiLogicScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var modelName by remember { mutableStateOf("gemini-3.5-flash") }
    var prompt by remember { mutableStateOf("Explain Kotlin Multiplatform in one sentence.") }
    var consoleLogs by remember { mutableStateOf("Console initialized.\n") }
    var isLoading by remember { mutableStateOf(false) }

    fun log(message: String) {
        consoleLogs += "${message}\n"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firebase AI Logic") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuration Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Model Settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = modelName,
                        onValueChange = { modelName = it },
                        label = { Text("Model Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("Prompt") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }

            // Action Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        log(">> Starting content generation request...")
                        log(">> Model: $modelName")
                        log(">> Prompt: \"$prompt\"")
                        try {
                            val firebaseAI = FirebaseApp.instance.ai
                            val model = firebaseAI.generativeModel(modelName)
                            log(">> Model instantiated. Sending generateContent call...")
                            val response = model.generateContent(prompt)
                            val textResult = response.text
                            log(">> Response received successfully!")
                            log(">> Result:\n$textResult")
                        } catch (e: UnsupportedOperationException) {
                            log(">> ERROR [Platform Unsupported]: ${e.message}")
                            log(">> NOTE: Firebase AI Logic is Stubbed on iOS because Swift-only framework linking is not supported via Kotlin Native cinterop.")
                        } catch (e: Exception) {
                            log(">> ERROR [Inference Failed]: ${e.message}")
                            log(">> Ensure Firebase project is setup, Gemini API is enabled, Google Services config is wired and billing is active.")
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && modelName.isNotEmpty() && prompt.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Generate Content")
                }
            }

            // Output Console Card
            Text(
                text = "Console Output",
                style = MaterialTheme.typography.titleMedium
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = consoleLogs,
                        color = Color.Green,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }
    }
}
