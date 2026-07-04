package zone.ien.firebase.inappmessaging

public expect class FirebaseInAppMessaging private constructor() {
    public var isAutomaticDataCollectionEnabled: Boolean
    public var areMessagesSuppressed: Boolean
    public fun triggerEvent(eventName: String)
    public companion object {
        public val instance: FirebaseInAppMessaging
    }
}
