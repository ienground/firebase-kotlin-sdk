package zone.ien.firebase.inappmessaging.display

public actual class FirebaseInAppMessagingDisplay private constructor() {
    public actual fun setCustomDisplayListener(listener: InAppMessagingDisplayListener) {
        throw UnsupportedOperationException("In-App Messaging Display component customization is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }

    public actual fun clearCustomDisplayListener() {
        throw UnsupportedOperationException("In-App Messaging Display component customization is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessagingDisplay
            get() = throw UnsupportedOperationException("In-App Messaging Display component customization is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }
}
