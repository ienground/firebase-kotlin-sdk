package zone.ien.firebase.example.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.ai.ai

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiLogicScreen(
    onNavigateBack: () -> Unit
) {
    // Detect iOS stub runtime exception or missing instance
    val aiResult = remember {
        if (FirebaseApp.isInitialized) {
            runCatching { FirebaseApp.instance.ai }
        } else {
            Result.failure(Exception("Firebase not initialized"))
        }
    }
    val isSupported = aiResult.isSuccess && aiResult.getOrNull() != null

    val coroutineScope = rememberCoroutineScope()
    var modelName by remember { mutableStateOf("gemini-3.5-flash") }
    var prompt by remember { mutableStateOf("Explain Kotlin Multiplatform in one sentence.") }
    var consoleLogs by remember { 
        mutableStateOf(
            if (!isSupported) "AI Logic is NOT supported on this platform: ${aiResult.exceptionOrNull()?.message}\n"
            else "Console initialized.\n"
        )
    }
    var isLoading by remember { mutableStateOf(false) }
    val consoleScrollState = rememberScrollState()
    LaunchedEffect(consoleLogs) {
        consoleScrollState.animateScrollTo(consoleScrollState.maxValue)
    }

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
            if (!isSupported) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "⚠️ Platform Not Supported",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Firebase AI Gemini model dispatcher is unavailable on this target due to stub platform migration constraints.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
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
                        text = "Model Settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSupported) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                    )

                    OutlinedTextField(
                        value = modelName,
                        enabled = isSupported,
                        onValueChange = { modelName = it },
                        label = { Text("Model Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = prompt,
                        enabled = isSupported,
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
                            if (!textResult.isNullOrBlank()) {
                                log(">> Result:\n$textResult")
                            } else {
                                log(">> Result: [Empty or Null Text]")
                            }
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
                enabled = isSupported && !isLoading && modelName.isNotEmpty() && prompt.isNotEmpty(),
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
                        .verticalScroll(consoleScrollState)
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
