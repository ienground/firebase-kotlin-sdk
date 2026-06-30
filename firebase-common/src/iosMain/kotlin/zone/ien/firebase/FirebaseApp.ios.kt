package zone.ien.firebase

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.common.FIRApp

actual class FirebasePlatformContext

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp private constructor(private val iosApp: FIRApp) {
    actual fun getName(): String = iosApp.name

    actual companion object {
        actual val isInitialized: Boolean
            get() = FIRApp.defaultApp() != null

        actual val instance: FirebaseApp
            get() {
                val app = FIRApp.defaultApp() ?: throw IllegalStateException("Firebase has not been configured yet.")
                return FirebaseApp(app)
            }

        actual fun initialize(context: FirebasePlatformContext): FirebaseApp {
            if (!isInitialized) {
                FIRApp.configure()
            }
            return instance
        }
    }
}
