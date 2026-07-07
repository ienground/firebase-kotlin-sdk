package zone.ien.firebase.inappmessaging.display

public expect class FirebaseInAppMessagingDisplay private constructor() {
    public fun setCustomDisplayListener(listener: InAppMessagingDisplayListener)
    public fun clearCustomDisplayListener()

    public companion object {
        public val instance: FirebaseInAppMessagingDisplay
    }
}
