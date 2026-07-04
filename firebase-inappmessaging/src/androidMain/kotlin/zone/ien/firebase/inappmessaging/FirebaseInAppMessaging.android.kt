package zone.ien.firebase.inappmessaging

public actual class FirebaseInAppMessaging internal constructor(
    private val androidInAppMessaging: com.google.firebase.inappmessaging.FirebaseInAppMessaging
) {
    public actual var isAutomaticDataCollectionEnabled: Boolean
        get() = androidInAppMessaging.isAutomaticDataCollectionEnabled
        set(value) {
            androidInAppMessaging.isAutomaticDataCollectionEnabled = value
        }

    public actual var areMessagesSuppressed: Boolean
        get() = androidInAppMessaging.areMessagesSuppressed()
        set(value) {
            androidInAppMessaging.setMessagesSuppressed(value)
        }

    public actual fun triggerEvent(eventName: String) {
        androidInAppMessaging.triggerEvent(eventName)
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessaging
            get() = FirebaseInAppMessaging(com.google.firebase.inappmessaging.FirebaseInAppMessaging.getInstance())
    }
}
