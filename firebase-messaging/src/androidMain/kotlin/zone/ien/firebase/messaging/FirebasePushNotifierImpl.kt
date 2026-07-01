package zone.ien.firebase.messaging

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

internal class FirebasePushNotifierImpl : PushNotifier {

    override suspend fun getToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            println("Error while getting token: $e")
            null
        }
    }

    override suspend fun deleteMyToken() {
        FirebaseMessaging.getInstance().deleteToken().await()
    }

    override suspend fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).await()
    }

    override suspend fun unSubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).await()
    }
}
