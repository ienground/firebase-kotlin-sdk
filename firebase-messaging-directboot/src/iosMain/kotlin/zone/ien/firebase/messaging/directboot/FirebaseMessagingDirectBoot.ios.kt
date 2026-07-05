package zone.ien.firebase.messaging.directboot

import zone.ien.firebase.FirebaseApp

public actual class FirebaseMessagingDirectBoot {
    public actual val isSupported: Boolean
        get() = false

    public actual fun getDeviceProtectedStorageContext(): Any? {
        return null
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseMessagingDirectBoot {
            return FirebaseMessagingDirectBoot()
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot {
            return FirebaseMessagingDirectBoot()
        }
    }
}
