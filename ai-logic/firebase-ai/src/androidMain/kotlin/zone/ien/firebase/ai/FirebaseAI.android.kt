package zone.ien.firebase.ai

import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.GenerateContentResponse as AndroidGenerateContentResponse
import zone.ien.firebase.FirebaseApp

public actual class FirebaseAI internal constructor(
    internal val androidAI: AndroidFirebaseAI
) {
    public actual fun generativeModel(modelName: String): GenerativeModel {
        val androidModel = androidAI.generativeModel(modelName)
        return GenerativeModel(androidModel)
    }

    public actual companion object {
        public actual val instance: FirebaseAI
            get() = FirebaseAI(AndroidFirebaseAI.getInstance(com.google.firebase.FirebaseApp.getInstance()))

        public actual fun getInstance(app: FirebaseApp): FirebaseAI {
            return FirebaseAI(AndroidFirebaseAI.getInstance(app.androidApp))
        }
    }
}

public actual val FirebaseApp.ai: FirebaseAI
    get() = FirebaseAI.getInstance(this)

public actual class GenerativeModel internal constructor(
    internal val androidModel: AndroidGenerativeModel
) {
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        val androidResponse = androidModel.generateContent(prompt)
        return GenerateContentResponse(androidResponse)
    }
}

public actual class GenerateContentResponse internal constructor(
    internal val androidResponse: AndroidGenerateContentResponse
) {
    public actual val text: String?
        get() = androidResponse.text
}
