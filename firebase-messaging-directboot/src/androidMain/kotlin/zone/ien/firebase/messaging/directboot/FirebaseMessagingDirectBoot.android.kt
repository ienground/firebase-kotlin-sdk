package zone.ien.firebase.messaging.directboot

import android.content.Context
import zone.ien.firebase.FirebaseApp

public actual class FirebaseMessagingDirectBoot(private val context: Context) {
    public actual val isSupported: Boolean
        get() = true

    public actual fun getDeviceProtectedStorageContext(): Any? {
        return context.createDeviceProtectedStorageContext()
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseMessagingDirectBoot {
            val app = FirebaseApp.instance
            return FirebaseMessagingDirectBoot(app.androidApp.applicationContext)
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot {
            return FirebaseMessagingDirectBoot(app.androidApp.applicationContext)
        }
    }
}
