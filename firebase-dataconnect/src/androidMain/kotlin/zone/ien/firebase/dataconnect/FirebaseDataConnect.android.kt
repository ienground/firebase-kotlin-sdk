package zone.ien.firebase.dataconnect

import com.google.firebase.dataconnect.getInstance
import zone.ien.firebase.FirebaseApp

public actual class FirebaseDataConnect internal constructor(
    private val androidDataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
) {
    public actual val config: ConnectorConfig
        get() = ConnectorConfig(
            service = androidDataConnect.config.serviceId,
            location = androidDataConnect.config.location,
            connector = androidDataConnect.config.connector
        )

    public actual fun useEmulator(host: String, port: Int) {
        androidDataConnect.useEmulator(host, port)
    }

    public actual companion object {
        private fun ConnectorConfig.toAndroid(): com.google.firebase.dataconnect.ConnectorConfig {
            return com.google.firebase.dataconnect.ConnectorConfig(service, location, connector)
        }

        public actual fun getInstance(config: ConnectorConfig): FirebaseDataConnect =
            FirebaseDataConnect(
                com.google.firebase.dataconnect.FirebaseDataConnect.getInstance(
                    com.google.firebase.FirebaseApp.getInstance(),
                    config.toAndroid()
                )
            )

        public actual fun getInstance(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect =
            FirebaseDataConnect(
                com.google.firebase.dataconnect.FirebaseDataConnect.getInstance(
                    app.androidApp,
                    config.toAndroid()
                )
            )
    }
}
