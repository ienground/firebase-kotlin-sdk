package zone.ien.firebase

/**
 * Platform-abstracted context for initializing platform SDKs.
 */
expect class FirebasePlatformContext

expect class FirebaseApp {
    fun getName(): String

    companion object {
        val instance: FirebaseApp
        val isInitialized: Boolean
        fun initialize(context: FirebasePlatformContext): FirebaseApp
    }
}
