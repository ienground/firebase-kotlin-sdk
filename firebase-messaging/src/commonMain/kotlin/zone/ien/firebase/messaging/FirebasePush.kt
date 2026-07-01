package zone.ien.firebase.messaging

public typealias PayloadData = Map<String, Any?>

public interface NotificationFormatter {
    public fun format(data: PayloadData, title: String?, body: String?): NotificationContent?
}

public class DefaultNotificationFormatter : NotificationFormatter {
    override fun format(data: PayloadData, title: String?, body: String?): NotificationContent? {
        if (title != null || body != null) {
            return NotificationContent(title, body)
        }
        return null
    }
}

public object KMPNotifier {

    private var listeners = setOf<PushListener>()

    // Allow setting the dynamic display mode (AUTO_DISPLAY or CALLBACK_ONLY)
    public var displayMode: PushDisplayMode = PushDisplayMode.AUTO_DISPLAY

    // Client-injected dynamic formatter (defaults to DefaultNotificationFormatter)
    public var notificationFormatter: NotificationFormatter? = DefaultNotificationFormatter()

    // Swift compatibility instance mapping
    public val shared: KMPNotifier = this

    internal val eventSink: PushEventSink = object : PushEventSink {
        override fun onNewToken(token: String) {
            listeners.toList().forEach { it.onNewToken(token) }
        }

        override fun onPushPayloadData(data: PayloadData) {
            println("onPushPayloadData ${data}")
            listeners.toList().forEach { it.onPayloadData(data) }
        }

        override fun onPushNotification(title: String?, body: String?) {
            println("onPushNotification ${title} $body")
            listeners.toList().forEach { it.onPushNotification(title = title, body = body) }
        }

        override fun onPushNotificationWithPayloadData(title: String?, body: String?, data: PayloadData) {
            println("onPushNotificationWithPayloadData ${title} $body $data")
            listeners.toList().forEach {
                it.onPushNotificationWithPayloadData(title = title, body = body, data = data)
            }
        }
    }

    public val notifier: PushNotifier by lazy {
        createFirebasePushNotifier()
    }

    public fun addPushListener(listener: PushListener) {
        listeners = listeners + listener
    }

    public fun removePushListener(listener: PushListener) {
        listeners = listeners - listener
    }

    @Deprecated("Use addPushListener", ReplaceWith("addPushListener(listener)"))
    public fun addListener(listener: PushListener) {
        addPushListener(listener)
    }

    @Deprecated("Use removePushListener", ReplaceWith("removePushListener(listener)"))
    public fun removeListener(listener: PushListener) {
        removePushListener(listener)
    }

    public fun setListener(listener: PushListener?) {
        listeners = if (listener != null) setOf(listener) else emptySet()
    }
}

internal interface PushEventSink {
    fun onNewToken(token: String)
    fun onPushPayloadData(data: PayloadData)
    fun onPushNotification(title: String?, body: String?)
    fun onPushNotificationWithPayloadData(title: String?, body: String?, data: PayloadData)
}

internal expect fun createFirebasePushNotifier(): PushNotifier
