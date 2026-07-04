package zone.ien.firebase.inappmessaging

public actual class FirebaseInAppMessaging private constructor() {
    public actual var isAutomaticDataCollectionEnabled: Boolean
        get() = throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
        set(value) {
            throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
        }

    public actual var areMessagesSuppressed: Boolean
        get() = throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
        set(value) {
            throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
        }

    public actual fun triggerEvent(eventName: String) {
        throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessaging
            get() = throw UnsupportedOperationException("Firebase In-App Messaging is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }
}
