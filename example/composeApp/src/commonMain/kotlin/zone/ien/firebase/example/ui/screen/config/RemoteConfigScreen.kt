package zone.ien.firebase.example.ui.screen.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import zone.ien.firebase.remoteconfig.FirebaseRemoteConfig
import zone.ien.firebase.remoteconfig.FirebaseRemoteConfigSettings
import zone.ien.firebase.remoteconfig.configUpdates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteConfigScreen(
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var testKey by remember { mutableStateOf("welcome_message") }
    var fetchedStringValue by remember { mutableStateOf("N/A") }
    var fetchedBooleanValue by remember { mutableStateOf("N/A") }
    var fetchedLongValue by remember { mutableStateOf("N/A") }
    var fetchedDoubleValue by remember { mutableStateOf("N/A") }
    var valueSource by remember { mutableStateOf("N/A") }

    var lastFetchStatus by remember { mutableStateOf("N/A") }
    var lastFetchTime by remember { mutableStateOf("N/A") }

    var minimumFetchInterval by remember { mutableStateOf("60") }
    var fetchTimeout by remember { mutableStateOf("15") }

    var logMessage by remember { mutableStateOf("Welcome to Firebase Remote Config console.") }
    var updatedKeysLog by remember { mutableStateOf("") }

    var isListening by remember { mutableStateOf(false) }
    var listenerJob by remember { mutableStateOf<Job?>(null) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    fun updateConfigInfo() {
        try {
            val info = FirebaseRemoteConfig.instance.getInfo()
            lastFetchStatus = info.lastFetchStatus.name
            lastFetchTime = if (info.fetchTimeMillis == 0L) "Never" else "${info.fetchTimeMillis}"
        } catch (e: Exception) {
            logMessage = "Failed to load config info: ${e.message}"
        }
    }

    LaunchedEffect(Unit) {
        updateConfigInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Config", fontWeight = FontWeight.Bold) },
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
                text = "Firebase Remote Config 데모",
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
                        "⚠️ 주의 사항 및 사용 지침",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1. 실시간 업데이트(Real-time Config Update) 기능을 로컬에서 테스트하려면 Firebase Console의 Remote Config 템플릿에 변경사항을 입력하고 '게시'해야 합니다.\n" +
                        "2. Firebase 초기화가 정상적으로 완료되어 있어야 하며, 네트워크 연결 상태에 따라 Fetch 및 Activate 시간이 다를 수 있습니다.\n" +
                        "3. 설정한 최소 Fetch 간격(minimumFetchInterval)보다 잦은 Fetch 호출은 서버로부터 THROTTLED 예외를 발생시킬 수 있습니다.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Defaults Settings Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. 기본값(Defaults) 설정", fontWeight = FontWeight.Bold)
                    Text("코드 기반으로 로컬 Fallback 기본값을 설정합니다.", fontSize = 12.sp)
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val defaults = mapOf(
                                        "welcome_message" to "Hello World from local default!",
                                        "is_feature_enabled" to true,
                                        "app_theme_color" to 0xFFFFFF00L
                                    )
                                    FirebaseRemoteConfig.instance.setDefaults(defaults)
                                    logMessage = "Local defaults set successfully!\nKey: welcome_message -> 'Hello World from local default!'\nKey: is_feature_enabled -> true"
                                } catch (e: Exception) {
                                    logMessage = "Failed to set defaults: ${e.message}"
                                }
                            }
                        }
                    ) {
                        Text("Set Defaults")
                    }
                }
            }

            // Settings Configurations Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("2. Config Settings 변경", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = minimumFetchInterval,
                            onValueChange = { minimumFetchInterval = it },
                            label = { Text("Min Fetch Interval (s)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = fetchTimeout,
                            onValueChange = { fetchTimeout = it },
                            label = { Text("Timeout (s)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val settings = FirebaseRemoteConfigSettings.Builder()
                                        .setMinimumFetchIntervalInSeconds(minimumFetchInterval.toLongOrNull() ?: 60L)
                                        .setFetchTimeoutInSeconds(fetchTimeout.toLongOrNull() ?: 15L)
                                        .build()
                                    FirebaseRemoteConfig.instance.setSettings(settings)
                                    logMessage = "Config Settings updated successfully."
                                    updateConfigInfo()
                                } catch (e: Exception) {
                                    logMessage = "Failed to set settings: ${e.message}"
                                }
                            }
                        }
                    ) {
                        Text("Update Settings")
                    }
                }
            }

            // Fetch & Activate Operations Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("3. Fetch & Activate 제어", fontWeight = FontWeight.Bold)
                    Text("서버의 최신 원격 구성을 호출하고 로컬 템플릿에 활성화합니다.", fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        logMessage = "Fetching remote config..."
                                        val success = FirebaseRemoteConfig.instance.fetch()
                                        logMessage = "Fetch Completed! Status: $success"
                                        updateConfigInfo()
                                    } catch (e: Exception) {
                                        logMessage = "Fetch Failed: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fetch")
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        logMessage = "Activating config..."
                                        val changed = FirebaseRemoteConfig.instance.activate()
                                        logMessage = "Activate Completed! Template changed? $changed"
                                        updateConfigInfo()
                                    } catch (e: Exception) {
                                        logMessage = "Activate Failed: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Activate")
                        }
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    logMessage = "Fetching and Activating config..."
                                    val success = FirebaseRemoteConfig.instance.fetchAndActivate()
                                    logMessage = "Fetch & Activate Completed! Success? $success"
                                    updateConfigInfo()
                                } catch (e: Exception) {
                                    logMessage = "Fetch & Activate Failed: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fetch and Activate")
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("마지막 Fetch 상태: $lastFetchStatus", fontSize = 12.sp)
                    Text("마지막 Fetch 시각: $lastFetchTime", fontSize = 12.sp)
                }
            }

            // Real-time config updates Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("4. 실시간 Config 업데이트 리스너", fontWeight = FontWeight.Bold)
                    Text("원격 백엔드의 실시간 템플릿 변경 이벤트를 감지합니다.", fontSize = 12.sp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                if (isListening) {
                                    listenerJob?.cancel()
                                    isListening = false
                                    logMessage = "Real-time update listener detached."
                                } else {
                                    isListening = true
                                    logMessage = "Listening for remote config changes in real-time..."
                                    listenerJob = coroutineScope.launch {
                                        FirebaseRemoteConfig.instance.configUpdates
                                            .catch { e ->
                                                logMessage = "Error in stream: ${e.message}"
                                                isListening = false
                                            }
                                            .collect { update ->
                                                val keys = update.updatedKeys.joinToString(", ")
                                                updatedKeysLog = "Updated keys: $keys"
                                                logMessage = "Received update event! Updated keys: $keys. You should fetch & activate to apply."
                                            }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isListening) errorColor else primaryColor
                            )
                        ) {
                            Text(if (isListening) "Disconnect Listener" else "Connect Listener")
                        }

                        Text(
                            text = if (isListening) "🟢 Connected" else "🔴 Disconnected",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (updatedKeysLog.isNotEmpty()) {
                        Text(updatedKeysLog, fontSize = 12.sp, color = primaryColor)
                    }
                }
            }

            // Config Read Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("5. Config Value 조회 (Typed Accessor)", fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = testKey,
                        onValueChange = { testKey = it },
                        label = { Text("Query Parameter Key") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            try {
                                val value = FirebaseRemoteConfig.instance.getValue(testKey)
                                fetchedStringValue = value.asString()
                                fetchedBooleanValue = "${value.asBoolean()}"
                                fetchedLongValue = "${value.asLong()}"
                                fetchedDoubleValue = "${value.asDouble()}"
                                valueSource = value.source.name
                                logMessage = "Key '$testKey' queried successfully."
                            } catch (e: Exception) {
                                logMessage = "Failed to query key: ${e.message}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Query Value")
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("String Value: $fetchedStringValue", fontSize = 13.sp)
                    Text("Boolean Value: $fetchedBooleanValue", fontSize = 13.sp)
                    Text("Long Value: $fetchedLongValue", fontSize = 13.sp)
                    Text("Double Value: $fetchedDoubleValue", fontSize = 13.sp)
                    Text("Value Source: $valueSource", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Console Log Card
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
        }
    }
}
