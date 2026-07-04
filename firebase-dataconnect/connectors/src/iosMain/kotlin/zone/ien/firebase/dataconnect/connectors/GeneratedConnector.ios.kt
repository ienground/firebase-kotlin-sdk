package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.FirebaseDataConnect

public actual class GeneratedConnector private actual constructor() : FirebaseDataConnectConnector {
    public actual override val dataConnect: FirebaseDataConnect
        get() = throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS due to Swift-only cinterop compilation constraints.")

    public actual companion object {
        public actual val instance: GeneratedConnector
            get() = throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS due to Swift-only cinterop compilation constraints.")
    }
}
