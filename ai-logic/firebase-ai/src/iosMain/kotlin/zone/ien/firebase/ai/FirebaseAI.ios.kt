package zone.ien.firebase.ai

import kotlinx.coroutines.delay
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.InternalFirebaseApi

public actual class FirebaseAI private constructor() {
    public actual fun generativeModel(modelName: String): GenerativeModel {
        return GenerativeModel(modelName)
    }

    public actual companion object {
        private val lazyInstance = lazy { FirebaseAI() }

        public actual val instance: FirebaseAI
            get() = lazyInstance.value

        public actual fun getInstance(app: FirebaseApp): FirebaseAI {
            return lazyInstance.value
        }
    }
}

public actual val FirebaseApp.ai: FirebaseAI
    get() = FirebaseAI.getInstance(this)

public actual class GenerativeModel(private val modelName: String = "") {
    @InternalFirebaseApi
    public var extraConfig: String? = null

    @OptIn(InternalFirebaseApi::class)
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        delay(1500)
        val isOnDevice = extraConfig?.startsWith("on-device-mode:") == true
        val mode = if (isOnDevice) extraConfig?.substringAfter("on-device-mode:") else "Cloud"
        
        val reply = if (isOnDevice) {
            """
                [Simulated On-Device / Hybrid Gemini Reply (${modelName})]
                Inference Mode: ${mode}
                Received Prompt: "${prompt}"
                
                This is a mock on-device/hybrid AI response simulated in memory-only mode on iOS due to Swift-only cinterop compilation constraints.
                On Apple platforms (v12.13.0+), Firebase AI supports Apple Intelligence integration.
                To run live on-device inference, integrate the official FirebaseAILogic Swift SDK directly in your native Apple codebase.
            """.trimIndent()
        } else {
            """
                [Simulated Gemini Reply (${modelName})]
                Received Prompt: "${prompt}"
                
                This is a mock Gemini response simulated in memory-only mode on iOS due to Swift-only cinterop compilation constraints.
                To connect to live Vertex AI backend services, integrate the official FirebaseAILogic Swift SDK directly into your native Apple target.
            """.trimIndent()
        }
        return GenerateContentResponse(reply)
    }
}

public actual class GenerateContentResponse(
    public actual val text: String? = null
)