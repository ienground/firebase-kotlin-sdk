package zone.ien.firebase.messaging.directboot

import zone.ien.firebase.FirebaseApp

public actual class FirebaseMessagingDirectBoot private actual constructor() {
    public actual val isSupported: Boolean
        get() = false

    public actual fun getDeviceProtectedStorageContext(): Any? {
        return null
    }

    public actual companion object {
        private val instance = FirebaseMessagingDirectBoot()

        public actual fun getInstance(): FirebaseMessagingDirectBoot {
            return instance
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot {
            return instance
        }
    }
}
