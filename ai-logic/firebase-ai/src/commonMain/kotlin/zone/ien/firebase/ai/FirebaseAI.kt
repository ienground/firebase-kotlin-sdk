package zone.ien.firebase.ai

import zone.ien.firebase.FirebaseApp

public expect class FirebaseAI {
    public fun generativeModel(modelName: String): GenerativeModel

    public companion object {
        public val instance: FirebaseAI
        public fun getInstance(app: FirebaseApp): FirebaseAI
    }
}

public expect val FirebaseApp.ai: FirebaseAI
