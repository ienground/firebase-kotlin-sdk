package zone.ien.firebase.example.ui.screen.ml

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zone.ien.firebase.example.util.isIos
import zone.ien.firebase.ml.modeldownloader.CustomModel
import zone.ien.firebase.ml.modeldownloader.CustomModelDownloadConditions
import zone.ien.firebase.ml.modeldownloader.DownloadType
import zone.ien.firebase.ml.modeldownloader.FirebaseModelDownloader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelDownloaderScreen(
    onNavigateBack: () -> Unit
) {
    val isSupported = true
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var modelName by remember { mutableStateOf("my_custom_model") }
    var downloadType by remember { mutableStateOf(DownloadType.LOCAL_MODEL) }
    var requireWifi by remember { mutableStateOf(false) }
    var requireDeviceIdle by remember { mutableStateOf(false) }
    var requireCharging by remember { mutableStateOf(false) }

    var logMessage by remember { 
        mutableStateOf(
            if (isIos) "iOS Notice: Running in memory simulation mode due to Swift-only cinterop constraints."
            else "Ready to download Firebase Custom Models."
        ) 
    }
    var downloadedModelsList by remember { mutableStateOf<List<CustomModel>>(emptyList()) }

    var latestDownloadedModel by remember { mutableStateOf<CustomModel?>(null) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    LaunchedEffect(isSupported) {
        if (isSupported) {
            try {
                val models = FirebaseModelDownloader.instance.listDownloadedModels()
                downloadedModelsList = models.toList()
            } catch (e: Exception) {
                logMessage = "Failed to list models: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Model Downloader", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                        text = "Firebase Model Downloader iOS SDK is Swift-only and cannot be linked directly into KMP via cinterop. This KMP wrapper runs in memory-only mode on iOS (acting as a model registry). To download actual physical TFLite model files on iOS, integrate the native Swift SDK inside your native iOS target codebase.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Firebase Remote Model Downloader Demo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSupported) primaryColor else Color.Gray
            )

            // Alert Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "⚠️ Warnings and Guidelines",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1. A custom TFLite model file with name \"$modelName\" must be uploaded to the Machine Learning tab in the Firebase Console.\n" +
                        "2. On iOS, only the Wi-Fi constraint is respected, charging and idle conditions are treated as no-op.\n" +
                        "3. Model inference using TFLite interpreter is not handled by this module; we only access the model path for validation.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Input fields
            OutlinedTextField(
                value = modelName,
                enabled = isSupported,
                onValueChange = { modelName = it },
                label = { Text("Custom Model Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Download Type Selection
            Text("Download Type", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LOCAL_MODEL,
                        enabled = isSupported,
                        onClick = { downloadType = DownloadType.LOCAL_MODEL }
                    )
                    Text("LOCAL_MODEL (Local first, download if needed)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                        enabled = isSupported,
                        onClick = { downloadType = DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND }
                    )
                    Text("LOCAL_MODEL_UPDATE_IN_BACKGROUND (Background update)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LATEST_MODEL,
                        enabled = isSupported,
                        onClick = { downloadType = DownloadType.LATEST_MODEL }
                    )
                    Text("LATEST_MODEL (Always fetch latest version)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
            }

            // Conditions Selection
            Text("Download Conditions", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireWifi = !requireWifi }
                ) {
                    Checkbox(checked = requireWifi, enabled = isSupported, onCheckedChange = null)
                    Text("Require Wi-Fi (allowsCellularAccess = false)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireCharging = !requireCharging }
                ) {
                    Checkbox(checked = requireCharging, enabled = isSupported, onCheckedChange = null)
                    Text("Require Charging (Android Only)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireDeviceIdle = !requireDeviceIdle }
                ) {
                    Checkbox(checked = requireDeviceIdle, enabled = isSupported, onCheckedChange = null)
                    Text("Require Device Idle (Android Only)", color = if (isSupported) MaterialTheme.colorScheme.onSurface else Color.Gray)
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                logMessage = "Downloading model: $modelName..."
                                val conditions = CustomModelDownloadConditions.Builder().run {
                                    if (requireWifi) requireWifi()
                                    if (requireDeviceIdle) requireDeviceIdle()
                                    if (requireCharging) requireCharging()
                                    build()
                                }
                                val model = FirebaseModelDownloader.instance.getModel(modelName, downloadType, conditions)
                                latestDownloadedModel = model
                                logMessage = "Download Successful!\nHash: ${model.modelHash}\nPath: ${model.path}\nSize: ${model.size} bytes"
                                val models = FirebaseModelDownloader.instance.listDownloadedModels()
                                downloadedModelsList = models.toList()
                            } catch (e: Exception) {
                                logMessage = "Download Failed: ${e.message}"
                            }
                        }
                    },
                    enabled = isSupported,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Download")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                logMessage = "Deleting model: $modelName..."
                                FirebaseModelDownloader.instance.deleteDownloadedModel(modelName)
                                logMessage = "Model deleted successfully."
                                if (latestDownloadedModel?.name == modelName) {
                                    latestDownloadedModel = null
                                }
                                val models = FirebaseModelDownloader.instance.listDownloadedModels()
                                downloadedModelsList = models.toList()
                            } catch (e: Exception) {
                                logMessage = "Deletion Failed: ${e.message}"
                            }
                        }
                    },
                    enabled = isSupported,
                    colors = ButtonDefaults.buttonColors(containerColor = errorColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete Model")
                }
            }

            // Status and Log Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📜 Execution Console Log", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(logMessage, fontSize = 12.sp)
                }
            }

            // Downloaded Models List
            Text("🗂️ Downloaded Local Models", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            if (downloadedModelsList.isEmpty()) {
                Text("No models registered locally.", fontSize = 14.sp)
            } else {
                downloadedModelsList.forEach { model ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Name: ${model.name}", fontWeight = FontWeight.Bold)
                            Text("Hash: ${model.modelHash}", fontSize = 11.sp)
                            Text("Path: ${model.path}", fontSize = 11.sp)
                            Text("Size: ${model.size} bytes", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
