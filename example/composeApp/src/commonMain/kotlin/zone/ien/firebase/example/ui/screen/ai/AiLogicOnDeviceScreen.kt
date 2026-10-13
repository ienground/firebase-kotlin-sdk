package zone.ien.firebase.example.ui.screen.ai
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.ai.InferenceMode
import zone.ien.firebase.ai.OnDeviceConfig
import zone.ien.firebase.ai.ai
import zone.ien.firebase.ai.generativeModel
import zone.ien.firebase.example.util.isIos
import com.kyant.capsule.ContinuousRoundedRectangle

@OptIn(zone.ien.firebase.InternalFirebaseApi::class)
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

    IenScaffold(
        topBar = {
            IenTopAppBar(
                title = { IenText("AI On-Device (Hybrid)") },
                navigationIcon = { IenBackButton(onClick = onNavigateBack) }
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
                        text = "Firebase AI On-Device iOS SDK is Swift-only and cannot be linked directly into KMP via cinterop. This KMP wrapper runs in memory-only mode on iOS (acting as a hybrid simulation engine). To run live on-device inference utilizing Apple Intelligence, integrate the native Swift SDK inside your native iOS target codebase.",
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.brand
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Configuration Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenText(
                        text = "Hybrid Settings",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) IenTheme.colors.textSecondary else Color.Gray
                    )

                    IenTextField(
                        value = modelName,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { modelName = it },
                        label = "Fallback Cloud Model ID",
                        modifier = Modifier.fillMaxWidth()
                    )

                    IenText(
                        text = "Inference Mode",
                        style = IenTheme.typography.body1,
                        color = if (isSupported) IenTheme.colors.textSecondary else Color.Gray
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
                                IenDotCheckbox(checked = inferenceMode == mode, onCheckedChange = { }) 
                                Spacer(modifier = Modifier.width(8.dp))
                                IenText(
                                    text = mode.name,
                                    color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Prompt Input Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenText(
                        text = "Prompt Input",
                        style = IenTheme.typography.title2,
                        color = if (isSupported) IenTheme.colors.textPrimary else Color.Gray
                    )

                    IenTextField(
                        value = prompt,
                        state = IenTextFieldState(enabled = isSupported),
                        onValueChange = { prompt = it },
                        label = "Prompt Text",
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    IenButton(
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
                        state = IenButtonState(enabled = isSupported && !isLoading),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            IenCircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = IenTheme.colors.textPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IenText("Run Inference")
                        }
                    }
                }
            }

            // Console Logs Card
            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    IenText(
                        text = consoleLogs,
                        style = IenTheme.typography.body2,
                        color = IenTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}
