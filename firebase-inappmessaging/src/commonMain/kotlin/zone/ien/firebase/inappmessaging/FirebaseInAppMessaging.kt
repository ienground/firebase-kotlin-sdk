package zone.ien.firebase.inappmessaging

import zone.ien.firebase.Firebase

public expect class FirebaseInAppMessaging private constructor() {
    public var isAutomaticDataCollectionEnabled: Boolean
    public var areMessagesSuppressed: Boolean
    public fun triggerEvent(eventName: String)
    public companion object {
        public val instance: FirebaseInAppMessaging
    }
}

public val Firebase.inAppMessaging: FirebaseInAppMessaging
    get() = FirebaseInAppMessaging.instance
