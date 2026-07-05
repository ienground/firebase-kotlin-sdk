package zone.ien.firebase.messaging

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging as AndroidFirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.tasks.await

public actual class FirebaseMessaging(private val delegate: AndroidFirebaseMessaging) {

    public actual var isAutoInitEnabled: Boolean
        get() = delegate.isAutoInitEnabled
        set(value) {
            delegate.isAutoInitEnabled = value
        }

    public actual suspend fun getToken(): String? = delegate.token.await()

    public actual suspend fun deleteToken() {
        delegate.deleteToken().await()
    }

    public actual suspend fun subscribeToTopic(topic: String) {
        delegate.subscribeToTopic(topic).await()
    }

    public actual suspend fun unsubscribeFromTopic(topic: String) {
        delegate.unsubscribeFromTopic(topic).await()
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseMessaging {
            return FirebaseMessaging(AndroidFirebaseMessaging.getInstance())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessaging {
            // Android SDK's getInstance(FirebaseApp) is package-private.
            // We retrieve the component instance via FirebaseApp component container get() lookup.
            val messaging = app.androidApp.get(AndroidFirebaseMessaging::class.java)
            return FirebaseMessaging(messaging)
        }
    }
}

// Coroutines helper to await any GMS Task without play-services dependency errors
private suspend fun <T> Task<T>.awaitTask(): T? = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            cont.resume(task.result)
        } else {
            val exception = task.exception ?: Exception("FCM Task failed with unknown exception")
            cont.resumeWithException(exception)
        }
    }
}
