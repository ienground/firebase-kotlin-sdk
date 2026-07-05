package zone.ien.firebase.messaging.directboot

import zone.ien.firebase.FirebaseApp

public expect class FirebaseMessagingDirectBoot {
    public val isSupported: Boolean
    public fun getDeviceProtectedStorageContext(): Any?

    public companion object {
        public fun getInstance(): FirebaseMessagingDirectBoot
        public fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot
    }
}
