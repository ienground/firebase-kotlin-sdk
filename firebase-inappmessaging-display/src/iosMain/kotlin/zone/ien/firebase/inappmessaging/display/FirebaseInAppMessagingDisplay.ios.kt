package zone.ien.firebase.inappmessaging.display

public actual class FirebaseInAppMessagingDisplay private actual constructor() {
    private var customDisplayListener: InAppMessagingDisplayListener? = null

    public actual fun setCustomDisplayListener(listener: InAppMessagingDisplayListener) {
        this.customDisplayListener = listener
    }

    public actual fun clearCustomDisplayListener() {
        this.customDisplayListener = null
    }

    public actual companion object {
        private val lazyInstance = lazy { FirebaseInAppMessagingDisplay() }

        public actual val instance: FirebaseInAppMessagingDisplay
            get() = lazyInstance.value
    }
}
