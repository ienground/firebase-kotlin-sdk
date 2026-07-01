package zone.ien.firebase.messaging

public interface PushListener {
    public fun onNewToken(token: String) {}
    public fun onPayloadData(data: Map<String, String>) {}
    public fun onPushNotification(title: String?, body: String?) {}
    public fun onPushNotificationWithPayloadData(title: String?, body: String?, data: Map<String, String>) {}
}
