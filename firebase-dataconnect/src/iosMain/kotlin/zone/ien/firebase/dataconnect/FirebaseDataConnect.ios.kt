package zone.ien.firebase.dataconnect

import zone.ien.firebase.FirebaseApp

public actual class FirebaseDataConnect private constructor() {
    public actual val config: ConnectorConfig
        get() = throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS yet due to Swift-only Objective-C interop compilation constraints.")

    public actual fun useEmulator(host: String, port: Int) {
        throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS yet due to Swift-only Objective-C interop compilation constraints.")
    }

    public actual companion object {
        public actual fun getInstance(config: ConnectorConfig): FirebaseDataConnect {
            throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS yet due to Swift-only Objective-C interop compilation constraints.")
        }

        public actual fun getInstance(app: FirebaseApp, config: ConnectorConfig): FirebaseDataConnect {
            throw UnsupportedOperationException("Firebase Data Connect is not supported on iOS yet due to Swift-only Objective-C interop compilation constraints.")
        }
    }
}
