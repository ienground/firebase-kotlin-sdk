package zone.ien.firebase.messaging

// Unified push message container
public data class PushMessage(
    val data: PayloadData,
    val notification: NotificationContent?
)

public data class NotificationContent(
    val title: String?,
    val body: String?
)

public enum class PushDisplayMode {
    AUTO_DISPLAY,  // The SDK internally handles data payloads and issues UI notifications automatically
    CALLBACK_ONLY  // The SDK only invokes callbacks/flows, passing data payload styling directly to application
}
