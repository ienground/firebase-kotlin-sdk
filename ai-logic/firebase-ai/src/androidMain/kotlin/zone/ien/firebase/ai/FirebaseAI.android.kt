package zone.ien.firebase.ai

import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.GenerateContentResponse as AndroidGenerateContentResponse
import zone.ien.firebase.FirebaseApp
import zone.ien.firebase.InternalFirebaseApi

public actual class FirebaseAI @InternalFirebaseApi constructor(
    @property:InternalFirebaseApi public val androidAI: AndroidFirebaseAI
) {
    @OptIn(InternalFirebaseApi::class)
    public actual fun generativeModel(modelName: String): GenerativeModel {
        val androidModel = androidAI.generativeModel(modelName)
        return GenerativeModel(androidModel)
    }

    public actual companion object {
        public actual val instance: FirebaseAI
            get() = getInstance(FirebaseApp.instance)

        @OptIn(InternalFirebaseApi::class)
        public actual fun getInstance(app: FirebaseApp): FirebaseAI {
            return FirebaseAI(AndroidFirebaseAI.getInstance(app.androidApp))
        }
    }
}

public actual val FirebaseApp.ai: FirebaseAI
    get() = FirebaseAI.getInstance(this)

public actual class GenerativeModel public constructor(
    public val androidModel: AndroidGenerativeModel
) {
    public actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        val androidResponse = androidModel.generateContent(prompt)
        return GenerateContentResponse(androidResponse)
    }
}

public actual class GenerateContentResponse public constructor(
    public val androidResponse: AndroidGenerateContentResponse
) {
    public actual val text: String?
        get() = androidResponse.text
}
