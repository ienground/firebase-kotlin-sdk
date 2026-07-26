package zone.ien.firebase.inappmessaging.display

import zone.ien.firebase.Firebase

public expect class FirebaseInAppMessagingDisplay private constructor() {
    public fun setCustomDisplayListener(listener: InAppMessagingDisplayListener)
    public fun clearCustomDisplayListener()

    public companion object {
        public val instance: FirebaseInAppMessagingDisplay
    }
}

public val Firebase.inAppMessagingDisplay: FirebaseInAppMessagingDisplay
    get() = FirebaseInAppMessagingDisplay.instance
