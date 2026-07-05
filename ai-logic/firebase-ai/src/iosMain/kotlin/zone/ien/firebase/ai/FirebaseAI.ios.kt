package zone.ien.firebase.ai

import kotlinx.coroutines.delay
import zone.ien.firebase.FirebaseApp

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

public actual class GenerativeModel(private val modelName: String) {
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        delay(1500)
        val reply = """
            [Simulated Gemini Reply (${modelName})]
            Received Prompt: "${prompt}"
            
            This is a mock Gemini response simulated in memory-only mode on iOS due to Swift-only cinterop compilation constraints.
            To connect to live Vertex AI backend services, integrate the official FirebaseAILogic Swift SDK directly into your native Apple target.
        """.trimIndent()
        return GenerateContentResponse(reply)
    }
}

public actual class GenerateContentResponse(
    public actual val text: String?
)
