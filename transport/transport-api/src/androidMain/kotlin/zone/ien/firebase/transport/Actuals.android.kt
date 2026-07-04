package zone.ien.firebase.transport

import kotlin.reflect.KClass

public actual class Encoding private constructor(internal val androidEncoding: com.google.android.datatransport.Encoding) {
    public actual val name: String
        get() = androidEncoding.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Encoding) return false
        return androidEncoding == other.androidEncoding
    }

    override fun hashCode(): Int {
        return androidEncoding.hashCode()
    }

    override fun toString(): String {
        return "Encoding(name=$name)"
    }

    public actual companion object {
        public actual fun of(name: String): Encoding =
            Encoding(com.google.android.datatransport.Encoding.of(name))
    }
}
public actual enum class Priority(internal val androidPriority: com.google.android.datatransport.Priority) {
    DEFAULT(com.google.android.datatransport.Priority.DEFAULT),
    VERY_LOW(com.google.android.datatransport.Priority.VERY_LOW),
    HIGHEST(com.google.android.datatransport.Priority.HIGHEST)
}
public actual abstract class Event<T> protected actual constructor() {
    internal abstract val androidEvent: com.google.android.datatransport.Event<T>
    public actual abstract fun getPayload(): T
    public actual abstract fun getCode(): Int?
    public actual abstract fun getPriority(): Priority
    public actual companion object {
        public actual fun <T> ofTelemetry(payload: T): Event<T> = AndroidEventWrapper(com.google.android.datatransport.Event.ofTelemetry(payload))
        public actual fun <T> ofTelemetry(code: Int, payload: T): Event<T> = AndroidEventWrapper(com.google.android.datatransport.Event.ofTelemetry(code, payload))
        public actual fun <T> ofUrgent(payload: T): Event<T> = AndroidEventWrapper(com.google.android.datatransport.Event.ofUrgent(payload))
        public actual fun <T> ofUrgent(code: Int, payload: T): Event<T> = AndroidEventWrapper(com.google.android.datatransport.Event.ofUrgent(code, payload))
    }
}
public class AndroidEventWrapper<T>(override val androidEvent: com.google.android.datatransport.Event<T>) : Event<T>() {
    override fun getPayload(): T = androidEvent.payload
    override fun getCode(): Int? = androidEvent.code
    override fun getPriority(): Priority = when (androidEvent.priority) {
        com.google.android.datatransport.Priority.DEFAULT -> Priority.DEFAULT
        com.google.android.datatransport.Priority.VERY_LOW -> Priority.VERY_LOW
        com.google.android.datatransport.Priority.HIGHEST -> Priority.HIGHEST
    }
}
public actual interface Transformer<T, U> {
    public actual fun apply(input: T): U
}
public class AndroidTransformerAdapter<T, U>(private val transformer: Transformer<T, U>) : com.google.android.datatransport.Transformer<T, U> {
    override fun apply(input: T): U = transformer.apply(input)
}
public actual interface TransportScheduleCallback {
    public actual fun onSchedule(error: Exception?)
}
public class AndroidCallbackAdapter(private val callback: TransportScheduleCallback) : com.google.android.datatransport.TransportScheduleCallback {
    override fun onSchedule(error: java.lang.Exception?) = callback.onSchedule(error)
}
public actual interface Transport<T> {
    public actual fun send(event: Event<T>)
    public actual fun schedule(event: Event<T>, callback: TransportScheduleCallback)
}
public class AndroidTransportWrapper<T>(private val androidTransport: com.google.android.datatransport.Transport<T>) : Transport<T> {
    override fun send(event: Event<T>) = androidTransport.send((event as AndroidEventWrapper).androidEvent)
    override fun schedule(event: Event<T>, callback: TransportScheduleCallback) = androidTransport.schedule((event as AndroidEventWrapper).androidEvent, AndroidCallbackAdapter(callback))
}
public actual interface TransportFactory {
    public actual fun <T : Any> getTransport(name: String, payloadType: KClass<T>, transformer: Transformer<T, ByteArray>): Transport<T>
    public actual fun <T : Any> getTransport(name: String, payloadType: KClass<T>, encoding: Encoding, transformer: Transformer<T, ByteArray>): Transport<T>
}
public class AndroidTransportFactoryWrapper(private val androidFactory: com.google.android.datatransport.TransportFactory) : TransportFactory {
    override fun <T : Any> getTransport(name: String, payloadType: KClass<T>, transformer: Transformer<T, ByteArray>): Transport<T> =
        AndroidTransportWrapper(androidFactory.getTransport(name, payloadType.javaObjectType, AndroidTransformerAdapter(transformer)))
    override fun <T : Any> getTransport(name: String, payloadType: KClass<T>, encoding: Encoding, transformer: Transformer<T, ByteArray>): Transport<T> =
        AndroidTransportWrapper(androidFactory.getTransport(name, payloadType.javaObjectType, com.google.android.datatransport.Encoding.of(encoding.name), AndroidTransformerAdapter(transformer)))
}
