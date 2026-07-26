package zone.ien.firebase.messaging.directboot

import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class FirebaseMessagingDirectBoot private constructor() {
    public val isSupported: Boolean
    public fun getDeviceProtectedStorageContext(): Any?

    public companion object {
        public fun getInstance(): FirebaseMessagingDirectBoot
        public fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot
    }
}

public val Firebase.messagingDirectBoot: FirebaseMessagingDirectBoot
    get() = FirebaseMessagingDirectBoot.getInstance()

public fun Firebase.messagingDirectBoot(app: FirebaseApp): FirebaseMessagingDirectBoot =
    FirebaseMessagingDirectBoot.getInstance(app)
