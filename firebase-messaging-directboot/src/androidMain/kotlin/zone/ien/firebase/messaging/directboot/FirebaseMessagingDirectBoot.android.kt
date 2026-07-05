package zone.ien.firebase.messaging.directboot

import android.content.Context
import zone.ien.firebase.FirebaseApp

public actual class FirebaseMessagingDirectBoot private actual constructor() {
    private lateinit var context: Context

    internal constructor(context: Context) : this() {
        this.context = context
    }

    public actual val isSupported: Boolean
        get() = true

    public actual fun getDeviceProtectedStorageContext(): Any? {
        return context.createDeviceProtectedStorageContext()
    }

    public actual companion object {
        @Volatile
        private var defaultInstance: FirebaseMessagingDirectBoot? = null

        public actual fun getInstance(): FirebaseMessagingDirectBoot {
            return defaultInstance ?: synchronized(this) {
                defaultInstance ?: FirebaseMessagingDirectBoot(FirebaseApp.instance.androidApp.applicationContext).also { defaultInstance = it }
            }
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseMessagingDirectBoot {
            return if (app == FirebaseApp.instance) {
                getInstance()
            } else {
                FirebaseMessagingDirectBoot(app.androidApp.applicationContext)
            }
        }
    }
}
