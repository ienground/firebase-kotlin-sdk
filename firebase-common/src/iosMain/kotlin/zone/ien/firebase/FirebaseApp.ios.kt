package zone.ien.firebase

import kotlinx.cinterop.ExperimentalForeignApi
// Kotlin 2.4.0 SPM namespace structure: swiftPMImport.<group>.<project>.<ClangModule>
// Gradle group: zone.ien.firebase, Project: firebase-common (dash converted to underscore)
import swiftPMImport.zone.ien.firebase.firebase.common.FIRApp

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp private constructor(private val iosApp: FIRApp) {
    actual fun getName(): String = iosApp.name

    actual companion object {
        actual fun getInstance(): FirebaseApp {
            val app = FIRApp.defaultApp() ?: throw IllegalStateException("Firebase has not been configured yet.")
            return FirebaseApp(app)
        }

        actual fun initialize(): FirebaseApp {
            FIRApp.configure()
            return getInstance()
        }
    }
}
