package zone.ien.firebase

/**
 * Common entry point for Firebase SDK, mirroring com.google.firebase.FirebaseApp.
 */
expect class FirebaseApp {
    fun getName(): String

    companion object {
        fun getInstance(): FirebaseApp
        fun initialize(): FirebaseApp
    }
}
