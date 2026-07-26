package zone.ien.firebase.example.ui.screen.ml
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
import com.kyant.capsule.ContinuousRoundedRectangle

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

    val primaryColor = IenTheme.colors.brand
    val errorColor = IenTheme.colors.danger

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

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("Model Downloader", fontWeight = FontWeight.Bold) },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) }
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
                        .clip(ContinuousRoundedRectangle(8.dp))
                        .background(IenTheme.colors.brandWeak)
                        .padding(12.dp)
                ) {
                    IenText(
                        text = "ℹ️ iOS cinterop Bridge Notice",
                        style = IenTheme.typography.title2,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    IenText(
                        text = "Firebase Model Downloader iOS SDK is Swift-only and cannot be linked directly into KMP via cinterop. This KMP wrapper runs in memory-only mode on iOS (acting as a model registry). To download actual physical TFLite model files on iOS, integrate the native Swift SDK inside your native iOS target codebase.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.brand
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            IenText(
                text = "Firebase Remote Model Downloader Demo",
                
                fontWeight = FontWeight.Bold,
                color = if (isSupported) primaryColor else Color.Gray
            )

            // Alert Box
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText(
                        "⚠️ Warnings and Guidelines",
                        fontWeight = FontWeight.Bold,
                        color = IenTheme.colors.danger
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(
                        "1. A custom TFLite model file with name \"$modelName\" must be uploaded to the Machine Learning tab in the Firebase Console.\n" +
                        "2. On iOS, only the Wi-Fi constraint is respected, charging and idle conditions are treated as no-op.\n" +
                        "3. Model inference using TFLite interpreter is not handled by this module; we only access the model path for validation.",
                        
                        color = IenTheme.colors.danger
                    )
                }
            }

            // Input fields
            IenTextField(
                value = modelName,
                state = IenTextFieldState(enabled = isSupported),
                onValueChange = { modelName = it },
                label = "Custom Model Name",
                modifier = Modifier.fillMaxWidth()
            )

            // Download Type Selection
            IenText("Download Type", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IenDotCheckbox(checked = downloadType == DownloadType.LOCAL_MODEL, onCheckedChange = { if (it) { downloadType = DownloadType.LOCAL_MODEL }
                     })
                    IenText("LOCAL_MODEL (Local first, download if needed)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IenDotCheckbox(checked = downloadType == DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, onCheckedChange = { if (it) { downloadType = DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND }
                     })
                    IenText("LOCAL_MODEL_UPDATE_IN_BACKGROUND (Background update)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IenDotCheckbox(checked = downloadType == DownloadType.LATEST_MODEL, onCheckedChange = { if (it) { downloadType = DownloadType.LATEST_MODEL }
                     })
                    IenText("LATEST_MODEL (Always fetch latest version)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
            }

            // Conditions Selection
            IenText("Download Conditions", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireWifi = !requireWifi }
                ) {
                    IenCircleCheckbox(checked = requireWifi, enabled = isSupported, onCheckedChange = null)
                    IenText("Require Wi-Fi (allowsCellularAccess = false)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireCharging = !requireCharging }
                ) {
                    IenCircleCheckbox(checked = requireCharging, enabled = isSupported, onCheckedChange = null)
                    IenText("Require Charging (Android Only)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = isSupported) { requireDeviceIdle = !requireDeviceIdle }
                ) {
                    IenCircleCheckbox(checked = requireDeviceIdle, enabled = isSupported, onCheckedChange = null)
                    IenText("Require Device Idle (Android Only)", color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray)
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IenButton(
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
                    state = IenButtonState(enabled = isSupported),
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Download")
                }

                IenButton(
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
                    state = IenButtonState(enabled = isSupported),
                    
                    modifier = Modifier.weight(1f)
                ) {
                    IenText("Delete Model")
                }
            }

            // Status and Log Box
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IenText("📜 Execution Console Log", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    IenText(logMessage, )
                }
            }

            // Downloaded Models List
            IenText("🗂️ Downloaded Local Models", fontWeight = FontWeight.Bold, color = if (isSupported) primaryColor else Color.Gray)
            if (downloadedModelsList.isEmpty()) {
                IenText("No models registered locally.", )
            } else {
                downloadedModelsList.forEach { model ->
                    IenSurface(color = IenTheme.colors.surfaceVariant, 
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            IenText("Name: ${model.name}", fontWeight = FontWeight.Bold)
                            IenText("Hash: ${model.modelHash}", )
                            IenText("Path: ${model.path}", )
                            IenText("Size: ${model.size} bytes", )
                        }
                    }
                }
            }
        }
    }
}
