package zone.ien.firebase.transport.runtime

import zone.ien.firebase.transport.TransportFactory

public expect class TransportRuntime {
    public fun newFactory(destinationName: String): TransportFactory
    public fun newFactory(destination: Destination): TransportFactory

    public companion object {
        public fun initialize(context: Any)
        public fun getInstance(): TransportRuntime
    }
}
