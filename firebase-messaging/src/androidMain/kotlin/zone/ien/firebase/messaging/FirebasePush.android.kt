package zone.ien.firebase.messaging

internal actual fun createFirebasePushNotifier(): PushNotifier = FirebasePushNotifierImpl()
