package zone.ien.firebase.messaging

import zone.ien.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging as AndroidFirebaseMessaging

actual class FirebaseMessaging(private val androidMessaging: AndroidFirebaseMessaging) {
    actual suspend fun getToken(): String {
        return androidMessaging.token.await()
    }

    actual suspend fun deleteToken() {
        androidMessaging.deleteToken().await()
    }

    actual fun addOnMessageReceivedListener(listener: (Map<String, String>) -> Unit) {
        listeners.add(listener)
    }

    actual companion object {
        private val listeners = mutableListOf<(Map<String, String>) -> Unit>()

        fun notifyMessageReceived(data: Map<String, String>) {
            listeners.forEach { it(data) }
        }

        actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(AndroidFirebaseMessaging.getInstance())
        }

        actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            return FirebaseMessaging(app.androidApp.get(AndroidFirebaseMessaging::class.java))
        }
    }
}
