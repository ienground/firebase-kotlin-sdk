package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFirestoreSettings

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseFirestoreSettings(internal val iosSettings: FIRFirestoreSettings) {
    actual val host: String
        get() = iosSettings.host
    actual val isSslEnabled: Boolean
        get() = iosSettings.isSSLEnabled()
    actual val isPersistenceEnabled: Boolean
        get() = true
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun createSettings(host: String, isSslEnabled: Boolean, isPersistenceEnabled: Boolean): FirebaseFirestoreSettings {
    val settings = FIRFirestoreSettings()
    settings.host = host
    settings.setSslEnabled(isSslEnabled)
    return FirebaseFirestoreSettings(settings)
}
