package zone.ien.firebase.dataconnect

import zone.ien.firebase.FirebaseApp

public actual class FirebaseDataConnect private constructor(
    public actual val config: ConnectorConfig
) {
    public var emulatorHost: String? = null
        private set
    public var emulatorPort: Int? = null
        private set

    public actual fun useEmulator(host: String, port: Int) {
        this.emulatorHost = host
        this.emulatorPort = port
    }

    public actual companion object {
        private val lock = platform.Foundation.NSLock()
        private val instances = mutableMapOf<ConnectorConfig, FirebaseDataConnect>()

        public actual fun getInstance(config: ConnectorConfig): FirebaseDataConnect {
            lock.lock()
            try {
                return instances.getOrPut(config) { FirebaseDataConnect(config) }
            } finally {
                lock.unlock()
            }
        }

        public actual fun getInstance(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect {
            return getInstance(config)
        }
    }
}
