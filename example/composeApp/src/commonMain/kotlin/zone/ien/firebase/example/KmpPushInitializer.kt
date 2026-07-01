package zone.ien.firebase.example

import zone.ien.firebase.messaging.FirebasePush
import zone.ien.firebase.messaging.NotificationFormatter

public object KmpPushInitializer {
    public fun initialize(formatter: NotificationFormatter) {
        FirebasePush.notificationFormatter = formatter
    }
}
