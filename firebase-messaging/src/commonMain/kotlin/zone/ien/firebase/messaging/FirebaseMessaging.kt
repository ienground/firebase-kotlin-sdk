package zone.ien.firebase.messaging

import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class FirebaseMessaging {
    public var isAutoInitEnabled: Boolean
    public var isDeliveryMetricsExportToBigQueryEnabled: Boolean

    public suspend fun getToken(): String?
    public suspend fun deleteToken()
    public suspend fun subscribeToTopic(topic: String)
    public suspend fun unsubscribeFromTopic(topic: String)

    public companion object {
        public fun getInstance(): FirebaseMessaging
        public fun getInstance(app: FirebaseApp): FirebaseMessaging
    }
}

public class Notification(
    public val title: String?,
    public val body: String?
)

public class RemoteMessage(
    public val data: Map<String, String>,
    public val notification: Notification?
)

public val Firebase.messaging: FirebaseMessaging
    get() = FirebaseMessaging.getInstance()

public fun Firebase.messaging(app: FirebaseApp): FirebaseMessaging =
    FirebaseMessaging.getInstance(app)
