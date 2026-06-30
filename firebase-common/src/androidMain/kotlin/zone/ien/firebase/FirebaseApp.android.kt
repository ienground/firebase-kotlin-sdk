package zone.ien.firebase

import android.content.Context
import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual class FirebaseApp(private val androidApp: AndroidFirebaseApp) {
    actual fun getName(): String = androidApp.name

    actual companion object {
        fun initialize(context: Context): FirebaseApp {
            val androidApp = AndroidFirebaseApp.initializeApp(context)
                ?: throw IllegalStateException("FirebaseApp initialization failed")
            return FirebaseApp(androidApp)
        }

        actual fun getInstance(): FirebaseApp {
            return FirebaseApp(AndroidFirebaseApp.getInstance())
        }

        actual fun initialize(): FirebaseApp {
            // Parameterless initialize retrieves default instance on Android.
            // Full initialization on Android usually requires a Context.
            return getInstance()
        }
    }
}
