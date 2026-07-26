package zone.ien.firebase.dataconnect

import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class FirebaseDataConnect {
    public val config: ConnectorConfig
    public fun useEmulator(host: String, port: Int)

    public companion object {
        public fun getInstance(config: ConnectorConfig): FirebaseDataConnect
        public fun getInstance(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect
    }
}

public fun Firebase.dataConnect(config: ConnectorConfig): FirebaseDataConnect =
    FirebaseDataConnect.getInstance(config)

public fun Firebase.dataConnect(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect =
    FirebaseDataConnect.getInstance(app, config)
