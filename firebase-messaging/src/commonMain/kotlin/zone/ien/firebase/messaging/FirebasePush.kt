package zone.ien.firebase.messaging

public interface NotificationFormatter {
    public fun format(data: Map<String, String>, title: String?, body: String?): NotificationContent?
}

public class DefaultNotificationFormatter : NotificationFormatter {
    override fun format(data: Map<String, String>, title: String?, body: String?): NotificationContent? {
        if (title != null || body != null) {
            return NotificationContent(title, body)
        }
        return null
    }
}

public object FirebasePush {

    private var listeners = setOf<PushListener>()

    // Allow setting the dynamic display mode (AUTO_DISPLAY or CALLBACK_ONLY)
    public var displayMode: PushDisplayMode = PushDisplayMode.AUTO_DISPLAY

    // Client-injected dynamic formatter (defaults to DefaultNotificationFormatter)
    public var notificationFormatter: NotificationFormatter? = DefaultNotificationFormatter()

    internal val eventSink: PushEventSink = object : PushEventSink {
        override fun onNewToken(token: String) {
            listeners.toList().forEach { it.onNewToken(token) }
        }

        override fun onPushPayloadData(data: Map<String, String>) {
            println("onPushPayloadData ${data}")
            listeners.toList().forEach { it.onPayloadData(data) }
        }

        override fun onPushNotification(title: String?, body: String?) {
            println("onPushNotification ${title} $body")
            listeners.toList().forEach { it.onPushNotification(title = title, body = body) }
        }

        override fun onPushNotificationWithPayloadData(title: String?, body: String?, data: Map<String, String>) {
            println("onPushNotificationWithPayloadData ${title} $body $data")
            listeners.toList().forEach {
                it.onPushNotificationWithPayloadData(title = title, body = body, data = data)
            }
        }
    }

    public val notifier: PushNotifier by lazy {
        createFirebasePushNotifier()
    }

    public fun addListener(listener: PushListener) {
        listeners = listeners + listener
    }

    public fun removeListener(listener: PushListener) {
        listeners = listeners - listener
    }

    public fun setListener(listener: PushListener?) {
        listeners = if (listener != null) setOf(listener) else emptySet()
    }
}

internal interface PushEventSink {
    fun onNewToken(token: String)
    fun onPushPayloadData(data: Map<String, String>)
    fun onPushNotification(title: String?, body: String?)
    fun onPushNotificationWithPayloadData(title: String?, body: String?, data: Map<String, String>)
}

internal expect fun createFirebasePushNotifier(): PushNotifier
