package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.FirebaseDataConnect

public actual class GeneratedConnector private constructor() : FirebaseDataConnectConnector {
    public actual override val dataConnect: FirebaseDataConnect = FirebaseDataConnect.getInstance(defaultConfig)

    public actual companion object {
        private val defaultConfig = ConnectorConfig(
            service = "movies",
            location = "us-central1",
            connector = "movie-connector"
        )

        private val lazyInstance = lazy { GeneratedConnector() }

        public actual val instance: GeneratedConnector
            get() = lazyInstance.value
    }
}
