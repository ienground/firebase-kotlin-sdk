package zone.ien.firebase.example.ui.screen.ml

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zone.ien.firebase.ml.modeldownloader.FirebaseModelDownloader
import zone.ien.firebase.ml.modeldownloader.DownloadType
import zone.ien.firebase.ml.modeldownloader.CustomModelDownloadConditions
import zone.ien.firebase.ml.modeldownloader.CustomModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelDownloaderScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var modelName by remember { mutableStateOf("my_custom_model") }
    var downloadType by remember { mutableStateOf(DownloadType.LOCAL_MODEL) }
    var requireWifi by remember { mutableStateOf(false) }
    var requireDeviceIdle by remember { mutableStateOf(false) }
    var requireCharging by remember { mutableStateOf(false) }

    var logMessage by remember { mutableStateOf("Ready to download Firebase Custom Models.") }
    var downloadedModelsList by remember { mutableStateOf<List<CustomModel>>(emptyList()) }

    var latestDownloadedModel by remember { mutableStateOf<CustomModel?>(null) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    LaunchedEffect(Unit) {
        coroutineScope.launch {
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
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
            Text(
                text = "Firebase Remote Model Downloader 데모",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // Alert Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "⚠️ 주의 사항 및 지침",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1. 실제 모델 다운로드를 실행하기 위해서는 Firebase Console의 'Machine Learning' 탭에 해당 모델 이름(\"$modelName\")의 Custom TFLite 모델 파일이 업로드되어 있어야 합니다.\n" +
                        "2. iOS의 경우 다운로드 조건 중 Wi-Fi 제한 여부만 동작에 반영되며, 충전 필수 및 기기 대기 조건은 no-op으로 취급됩니다.\n" +
                        "3. TFLite 인터프리터를 통한 추론(Inference) 실행은 이 모듈의 책임 범위가 아니므로 경로 확인용으로만 모델 파일에 접근합니다.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Input fields
            OutlinedTextField(
                value = modelName,
                onValueChange = { modelName = it },
                label = { Text("Custom Model Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Download Type Selection
            Text("Download Type", fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LOCAL_MODEL,
                        onClick = { downloadType = DownloadType.LOCAL_MODEL }
                    )
                    Text("LOCAL_MODEL (로컬 우선, 필요 시 다운로드)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                        onClick = { downloadType = DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND }
                    )
                    Text("LOCAL_MODEL_UPDATE_IN_BACKGROUND (백그라운드 업데이트)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = downloadType == DownloadType.LATEST_MODEL,
                        onClick = { downloadType = DownloadType.LATEST_MODEL }
                    )
                    Text("LATEST_MODEL (항상 최신 버전 다운로드 대기)")
                }
            }

            // Conditions Selection
            Text("Download Conditions", fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = requireWifi, onCheckedChange = { requireWifi = it })
                    Text("Wi-Fi 필수 (allowsCellularAccess = false)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = requireCharging, onCheckedChange = { requireCharging = it })
                    Text("충전 중 필수 (Android 전용)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = requireDeviceIdle, onCheckedChange = { requireDeviceIdle = it })
                    Text("기기 대기 중 필수 (Android 전용)")
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
            Text("🗂️ Downloaded Local Models", fontWeight = FontWeight.Bold)
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
