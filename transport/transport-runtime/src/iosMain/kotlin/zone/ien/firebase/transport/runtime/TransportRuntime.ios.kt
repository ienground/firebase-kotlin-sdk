package zone.ien.firebase.transport.runtime

import zone.ien.firebase.transport.TransportFactory
import zone.ien.firebase.transport.IOSTransportFactory

public actual class TransportRuntime {
    public actual fun newFactory(destinationName: String): TransportFactory {
        return IOSTransportFactory()
    }

    public actual fun newFactory(destination: Destination): TransportFactory {
        return IOSTransportFactory()
    }

    public actual companion object {
        private val INSTANCE: TransportRuntime = TransportRuntime()

        public actual fun initialize(context: Any) {
            // no-op on iOS
        }

        public actual fun getInstance(): TransportRuntime {
            return INSTANCE
        }
    }
}
