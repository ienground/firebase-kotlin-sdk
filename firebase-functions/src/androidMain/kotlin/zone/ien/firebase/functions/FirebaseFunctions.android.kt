package zone.ien.firebase.functions

import zone.ien.firebase.FirebaseApp
import com.google.firebase.functions.FirebaseFunctions as AndroidFirebaseFunctions
import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual class FirebaseFunctions(private val androidFunctions: AndroidFirebaseFunctions) {
    actual fun getHttpsCallable(name: String): HttpsCallableReference {
        return HttpsCallableReference(androidFunctions.getHttpsCallable(name))
    }

    actual fun getHttpsCallableFromUrl(url: String): HttpsCallableReference {
        return HttpsCallableReference(androidFunctions.getHttpsCallableFromUrl(java.net.URL(url)))
    }

    actual fun useEmulator(host: String, port: Int) {
        androidFunctions.useEmulator(host, port)
    }

    actual companion object {
        actual fun getInstance(): FirebaseFunctions {
            return FirebaseFunctions(AndroidFirebaseFunctions.getInstance())
        }

        actual fun getInstance(app: FirebaseApp): FirebaseFunctions {
            return FirebaseFunctions(AndroidFirebaseFunctions.getInstance(app.androidApp))
        }

        actual fun getInstance(region: String): FirebaseFunctions {
            return FirebaseFunctions(AndroidFirebaseFunctions.getInstance(region))
        }

        actual fun getInstance(app: FirebaseApp, region: String): FirebaseFunctions {
            return FirebaseFunctions(AndroidFirebaseFunctions.getInstance(app.androidApp, region))
        }
    }
}
