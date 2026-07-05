package zone.ien.firebase.dataconnect

import zone.ien.firebase.FirebaseApp

public actual class FirebaseDataConnect private constructor(
    public actual val config: ConnectorConfig
) {
    private var emulatorHost: String? = null
    private var emulatorPort: Int? = null

    public actual fun useEmulator(host: String, port: Int) {
        this.emulatorHost = host
        this.emulatorPort = port
    }

    public actual companion object {
        public actual fun getInstance(config: ConnectorConfig): FirebaseDataConnect {
            return FirebaseDataConnect(config)
        }

        public actual fun getInstance(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect {
            return FirebaseDataConnect(config)
        }
    }
}
