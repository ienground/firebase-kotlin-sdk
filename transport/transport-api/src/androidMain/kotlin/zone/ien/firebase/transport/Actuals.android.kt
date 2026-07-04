package zone.ien.firebase.transport

public actual class Class<T>(internal val javaClass: java.lang.Class<T>)

public actual enum class Priority(internal val androidPriority: com.google.android.datatransport.Priority) {
    DEFAULT(com.google.android.datatransport.Priority.DEFAULT),
    VERY_LOW(com.google.android.datatransport.Priority.VERY_LOW),
    HIGHEST(com.google.android.datatransport.Priority.HIGHEST)
}

public actual class Encoding(internal val androidEncoding: com.google.android.datatransport.Encoding) {
    public actual val name: String
        get() = androidEncoding.name

    public actual companion object {
        public actual fun of(name: String): Encoding =
            Encoding(com.google.android.datatransport.Encoding.of(name))
    }
}

public actual abstract class Event<T>(internal val androidEvent: com.google.android.datatransport.Event<T>) {
    public actual abstract fun getPayload(): T
    public actual abstract fun getCode(): Int?
    public actual abstract fun getPriority(): Priority

    public actual companion object {
        public actual fun <T> ofTelemetry(payload: T): Event<T> {
            val android = com.google.android.datatransport.Event.ofTelemetry(payload)
            return AndroidEventWrapper(android)
        }
        public actual fun <T> ofTelemetry(code: Int, payload: T): Event<T> {
            val android = com.google.android.datatransport.Event.ofTelemetry(code, payload)
            return AndroidEventWrapper(android)
        }
        public actual fun <T> ofUrgent(payload: T): Event<T> {
            val android = com.google.android.datatransport.Event.ofUrgent(payload)
            return AndroidEventWrapper(android)
        }
        public actual fun <T> ofUrgent(code: Int, payload: T): Event<T> {
            val android = com.google.android.datatransport.Event.ofUrgent(code, payload)
            return AndroidEventWrapper(android)
        }
    }
}

internal class AndroidEventWrapper<T>(androidEvent: com.google.android.datatransport.Event<T>) : Event<T>(androidEvent) {
    override fun getPayload(): T = androidEvent.payload
    override fun getCode(): Int? = androidEvent.code
    override fun getPriority(): Priority = Priority.values().first { it.androidPriority == androidEvent.priority }
}

public actual interface Transformer<T, U> {
    public actual fun apply(input: T): U
}

internal class AndroidTransformerAdapter<T, U>(
    private val transformer: Transformer<T, U>
) : com.google.android.datatransport.Transformer<T, U> {
    override fun apply(input: T): U = transformer.apply(input)
}

public actual interface TransportScheduleCallback {
    public actual fun onSchedule(error: Exception?)
}

internal class AndroidCallbackAdapter(
    private val callback: TransportScheduleCallback
) : com.google.android.datatransport.TransportScheduleCallback {
    override fun onSchedule(error: java.lang.Exception?) {
        callback.onSchedule(error)
    }
}

public actual interface Transport<T> {
    public actual fun send(event: Event<T>)
    public actual fun schedule(event: Event<T>, callback: TransportScheduleCallback)
}

internal class AndroidTransportWrapper<T>(
    private val androidTransport: com.google.android.datatransport.Transport<T>
) : Transport<T> {
    override fun send(event: Event<T>) {
        androidTransport.send(event.androidEvent)
    }

    override fun schedule(event: Event<T>, callback: TransportScheduleCallback) {
        androidTransport.schedule(event.androidEvent, AndroidCallbackAdapter(callback))
    }
}

public actual interface TransportFactory {
    public actual fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>

    public actual fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>
}

internal class AndroidTransportFactoryWrapper(
    private val androidFactory: com.google.android.datatransport.TransportFactory
) : TransportFactory {
    override fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T> {
        val androidTransport = androidFactory.getTransport(
            name,
            payloadType.javaClass,
            AndroidTransformerAdapter(transformer)
        )
        return AndroidTransportWrapper(androidTransport)
    }

    override fun <T> getTransport(
        name: String,
        payloadType: Class<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T> {
        val androidTransport = androidFactory.getTransport(
            name,
            payloadType.javaClass,
            encoding.androidEncoding,
            AndroidTransformerAdapter(transformer)
        )
        return AndroidTransportWrapper(androidTransport)
    }
}
