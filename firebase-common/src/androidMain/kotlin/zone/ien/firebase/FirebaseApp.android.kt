package zone.ien.firebase

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual class FirebasePlatformContext(val androidContext: Context)

actual class FirebaseApp(val androidApp: AndroidFirebaseApp) {
    actual fun getName(): String = androidApp.name

    actual companion object {
        actual val isInitialized: Boolean
            get() = try {
                AndroidFirebaseApp.getInstance()
                true
            } catch (e: IllegalStateException) {
                false
            }

        actual val instance: FirebaseApp
            get() {
                val app = AndroidFirebaseApp.getInstance()
                return FirebaseApp(app)
            }

        actual fun initialize(context: FirebasePlatformContext): FirebaseApp {
            val appCtx = context.androidContext.applicationContext
            if (!isMainProcess(appCtx)) {
                throw IllegalStateException("FirebaseApp initialization is restricted to the main process.")
            }
            val androidApp = AndroidFirebaseApp.initializeApp(appCtx)
                ?: throw IllegalStateException("FirebaseApp initialization failed")
            return FirebaseApp(androidApp)
        }

        private fun isMainProcess(context: Context): Boolean {
            return android.app.Application.getProcessName() == context.packageName
        }
    }
}
