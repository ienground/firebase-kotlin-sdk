package zone.ien.firebase.example

import zone.ien.firebase.messaging.KMPNotifier
import zone.ien.firebase.messaging.NotificationContent
import zone.ien.firebase.messaging.formatNotification

public object AppNotificationServiceHelper {
    public fun formatNotification(userInfo: Map<Any?, Any?>): NotificationContent? {
        // Automatically inject the custom app-level formatter on first demand
        if (KMPNotifier.notificationFormatter == null || KMPNotifier.notificationFormatter is zone.ien.firebase.messaging.DefaultNotificationFormatter) {
            KMPNotifier.notificationFormatter = ExampleNotificationFormatter()
        }
        return KMPNotifier.formatNotification(userInfo)
    }
}
