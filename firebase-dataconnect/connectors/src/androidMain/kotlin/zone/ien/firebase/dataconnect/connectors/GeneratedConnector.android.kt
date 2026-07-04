package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.FirebaseDataConnect

public actual class GeneratedConnector private constructor(
    public actual override val dataConnect: FirebaseDataConnect
) : FirebaseDataConnectConnector {
    public actual companion object {
        private val defaultConfig = ConnectorConfig(
            service = "movies",
            location = "us-central1",
            connector = "movie-connector"
        )

        public actual val instance: GeneratedConnector
            get() = GeneratedConnector(FirebaseDataConnect.getInstance(defaultConfig))
    }
}
