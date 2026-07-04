package zone.ien.firebase.transport

import kotlin.reflect.KClass

public actual class Encoding private constructor(public actual val name: String) {
    public actual companion object {
        public actual fun of(name: String): Encoding = Encoding(name)
    }
}

public actual enum class Priority {
    DEFAULT,
    VERY_LOW,
    HIGHEST
}

public actual abstract class Event<T> protected actual constructor() {
    public actual abstract fun getPayload(): T
    public actual abstract fun getCode(): Int?
    public actual abstract fun getPriority(): Priority

    public actual companion object {
        public actual fun <T> ofTelemetry(payload: T): Event<T> = throw UnsupportedOperationException("Datatransport is not supported on iOS yet.")
        public actual fun <T> ofTelemetry(code: Int, payload: T): Event<T> = throw UnsupportedOperationException("Datatransport is not supported on iOS yet.")
        public actual fun <T> ofUrgent(payload: T): Event<T> = throw UnsupportedOperationException("Datatransport is not supported on iOS yet.")
        public actual fun <T> ofUrgent(code: Int, payload: T): Event<T> = throw UnsupportedOperationException("Datatransport is not supported on iOS yet.")
    }
}

public actual interface Transformer<T, U> {
    public actual fun apply(input: T): U
}

public actual interface TransportScheduleCallback {
    public actual fun onSchedule(error: Exception?)
}

public actual interface Transport<T> {
    public actual fun send(event: Event<T>)
    public actual fun schedule(event: Event<T>, callback: TransportScheduleCallback)
}

public actual interface TransportFactory {
    public actual fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>

    public actual fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T>
}
