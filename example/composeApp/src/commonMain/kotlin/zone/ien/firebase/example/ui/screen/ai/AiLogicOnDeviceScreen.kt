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
import zone.ien.firebase.ai.InferenceMode
import zone.ien.firebase.ai.OnDeviceConfig
import zone.ien.firebase.ai.ai
import zone.ien.firebase.ai.generativeModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiLogicOnDeviceScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var modelName by remember { mutableStateOf("gemini-3.5-flash") }
    var prompt by remember { mutableStateOf("Write a 3-word slogan for KMP.") }
    var inferenceMode by remember { mutableStateOf(InferenceMode.PREFER_ON_DEVICE) }
    var consoleLogs by remember { mutableStateOf("Console initialized for Hybrid AI.\n") }
    var isLoading by remember { mutableStateOf(false) }

    fun log(message: String) {
        consoleLogs += "${message}\n"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI On-Device (Hybrid)") },
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
                        text = "Hybrid Settings",
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
                        minLines = 2
                    )

                    Text(
                        text = "Inference Mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        InferenceMode.entries.forEach { mode ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = inferenceMode == mode,
                                    onClick = { inferenceMode = mode }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = mode.name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            // Action Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        log(">> Initiating Hybrid content generation...")
                        log(">> Model: $modelName")
                        log(">> Mode: $inferenceMode")
                        log(">> Prompt: \"$prompt\"")
                        try {
                            val firebaseAI = FirebaseApp.instance.ai
                            val onDeviceConfig = OnDeviceConfig(inferenceMode)
                            log(">> Configured with hybrid mode. Instantiating model...")
                            val model = firebaseAI.generativeModel(modelName, onDeviceConfig)
                            log(">> Model instantiated. Sending generateContent call...")
                            val response = model.generateContent(prompt)
                            val textResult = response.text
                            log(">> Response received successfully!")
                            log(">> Result:\n$textResult")
                        } catch (e: UnsupportedOperationException) {
                            log(">> ERROR [Platform Unsupported]: ${e.message}")
                            log(">> NOTE: On-Device / Hybrid AI is Stubbed on iOS due to Swift-only framework limitations.")
                        } catch (e: Exception) {
                            log(">> ERROR [Inference Failed]: ${e.message}")
                            log(">> On-device AI (Gemini Nano) requires AICore service availability. Cloud fallback will trigger if network is reachable and backend config is wired.")
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
                    Text("Generate (Hybrid)")
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
