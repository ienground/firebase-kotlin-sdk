package zone.ien.firebase.transport

import kotlin.reflect.KClass

public expect interface TransportFactory {
    public fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>
    public fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>
}