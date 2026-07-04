package zone.ien.firebase.transport

public expect interface TransportFactory {
    public fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>

    public fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>
}
