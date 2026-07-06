package zone.ien.firebase.example.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.ai.InferenceMode
import zone.ien.firebase.ai.OnDeviceConfig
import zone.ien.firebase.ai.ai
import zone.ien.firebase.ai.generativeModel
import zone.ien.firebase.example.util.isIos

@OptIn(ExperimentalMaterial3Api::class, zone.ien.firebase.InternalFirebaseApi::class)
@Composable
fun AiLogicOnDeviceScreen(
    onNavigateBack: () -> Unit
) {
    val isSupported = true
    val coroutineScope = rememberCoroutineScope()
    var modelName by remember { mutableStateOf("gemini-3.5-flash") }
    var prompt by remember { mutableStateOf("Write a 3-word slogan for KMP.") }
    var inferenceMode by remember { mutableStateOf(InferenceMode.PREFER_ON_DEVICE) }
    var consoleLogs by remember { 
        mutableStateOf(
            if (isIos) "iOS Notice: Running in on-device/hybrid memory simulation mode due to Swift-only cinterop constraints.\n"
            else "Console initialized for Hybrid AI.\n"
        )
    }
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
                }
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
            Spacer(modifier = Modifier.height(16.dp))
            if (isIos) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "ℹ️ iOS cinterop Bridge Notice",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Firebase AI On-Device iOS SDK is Swift-only and cannot be linked directly into KMP via cinterop. This KMP wrapper runs in memory-only mode on iOS (acting as a hybrid simulation engine). To run live on-device inference utilizing Apple Intelligence, integrate the native Swift SDK inside your native iOS target codebase.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

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
                        color = if (isSupported) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )

                    OutlinedTextField(
                        value = modelName,
                        enabled = isSupported,
                        onValueChange = { modelName = it },
                        label = { Text("Fallback Cloud Model ID") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Inference Mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSupported) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InferenceMode.entries.forEach { mode ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable(enabled = isSupported) {
                                        if (isSupported) inferenceMode = mode
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = inferenceMode == mode,
                                    enabled = isSupported,
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = mode.name,
                                    color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Prompt Input Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Prompt Input",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )

                    OutlinedTextField(
                        value = prompt,
                        enabled = isSupported,
                        onValueChange = { prompt = it },
                        label = { Text("Prompt Text") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    isLoading = true
                                    log(">> Starting content generation request...")
                                    log(">> Mode: $inferenceMode")
                                    log(">> Prompt: \"$prompt\"")
                                    val fallbackModel = FirebaseApp.instance.ai.generativeModel(
                                        modelName = modelName,
                                        onDeviceConfig = OnDeviceConfig(
                                            mode = inferenceMode
                                        )
                                    )
                                    val response = fallbackModel.generateContent(prompt)
                                    log(">> Response received successfully!")
                                    log(">> Result:\n${response.text}")
                                } catch (e: UnsupportedOperationException) {
                                    log(">> ERROR [Platform Unsupported]: ${e.message}")
                                    log(">> NOTE: AI On-Device is stubbed on iOS because Swift-only framework dependencies cannot be linked.")
                                } catch (e: Exception) {
                                    log(">> ERROR [Inference Failed]: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = isSupported && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Run Inference")
                        }
                    }
                }
            }

            // Console Logs Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = consoleLogs,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
