package zone.ien.firebase

/**
 * Main entry point for Firebase SDKs.
 */
public object Firebase

public val Firebase.app: FirebaseApp
    get() = FirebaseApp.instance

public fun Firebase.initialize(context: FirebasePlatformContext): FirebaseApp =
    FirebaseApp.initialize(context)
