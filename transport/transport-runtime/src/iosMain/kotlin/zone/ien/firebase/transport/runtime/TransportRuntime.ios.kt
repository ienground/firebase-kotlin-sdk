package zone.ien.firebase.transport.runtime

import zone.ien.firebase.transport.TransportFactory

public actual class TransportRuntime {
    public actual fun newFactory(destinationName: String): TransportFactory {
        throw UnsupportedOperationException("TransportRuntime is not supported on iOS yet.")
    }

    public actual fun newFactory(destination: Destination): TransportFactory {
        throw UnsupportedOperationException("TransportRuntime is not supported on iOS yet.")
    }

    public actual companion object {
        public actual fun initialize(context: Any) {
            // no-op on iOS
        }

        public actual fun getInstance(): TransportRuntime {
            throw UnsupportedOperationException("TransportRuntime is not supported on iOS yet.")
        }
    }
}
