package zone.ien.firebase.inappmessaging

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessaging

public actual class FirebaseInAppMessaging private actual constructor() {
    @OptIn(ExperimentalForeignApi::class)
    private val delegate: FIRInAppMessaging
        get() = FIRInAppMessaging.inAppMessaging()

    @OptIn(ExperimentalForeignApi::class)
    public actual var isAutomaticDataCollectionEnabled: Boolean
        get() = delegate.automaticDataCollectionEnabled
        set(value) {
            delegate.automaticDataCollectionEnabled = value
        }

    @OptIn(ExperimentalForeignApi::class)
    public actual var areMessagesSuppressed: Boolean
        get() = delegate.messageDisplaySuppressed
        set(value) {
            delegate.messageDisplaySuppressed = value
        }

    @OptIn(ExperimentalForeignApi::class)
    public actual fun triggerEvent(eventName: String) {
        delegate.triggerEvent(eventName)
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessaging by lazy { FirebaseInAppMessaging() }
    }
}
