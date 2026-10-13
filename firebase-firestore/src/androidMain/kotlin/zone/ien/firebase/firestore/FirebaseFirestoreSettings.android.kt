package zone.ien.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreSettings as AndroidFirebaseFirestoreSettings

actual class FirebaseFirestoreSettings(internal val androidSettings: AndroidFirebaseFirestoreSettings) {
    actual val host: String
        get() = androidSettings.host
    actual val isSslEnabled: Boolean
        get() = androidSettings.isSslEnabled
    actual val isPersistenceEnabled: Boolean
        get() = androidSettings.isPersistenceEnabled
}

internal actual fun createSettings(host: String, isSslEnabled: Boolean, isPersistenceEnabled: Boolean): FirebaseFirestoreSettings {
    val androidSettings = AndroidFirebaseFirestoreSettings.Builder()
        .setHost(host)
        .setSslEnabled(isSslEnabled)
        .setPersistenceEnabled(isPersistenceEnabled)
        .build()
    return FirebaseFirestoreSettings(androidSettings)
}
