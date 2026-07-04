package zone.ien.firebase.ai

import zone.ien.firebase.FirebaseApp

public actual class FirebaseAI {
    public actual fun generativeModel(modelName: String): GenerativeModel {
        throw UnsupportedOperationException("Firebase AI is not supported on iOS yet.")
    }

    public actual companion object {
        public actual val instance: FirebaseAI
            get() = FirebaseAI()

        public actual fun getInstance(app: FirebaseApp): FirebaseAI {
            return FirebaseAI()
        }
    }
}

public actual val FirebaseApp.ai: FirebaseAI
    get() = FirebaseAI.getInstance(this)

public actual class GenerativeModel {
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        throw UnsupportedOperationException("Firebase AI is not supported on iOS yet.")
    }
}

public actual class GenerateContentResponse {
    public actual val text: String?
        get() = throw UnsupportedOperationException("Firebase AI is not supported on iOS yet.")
}
