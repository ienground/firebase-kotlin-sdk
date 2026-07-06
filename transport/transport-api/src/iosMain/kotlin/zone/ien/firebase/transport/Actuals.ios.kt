package zone.ien.firebase.transport

import kotlin.reflect.KClass

public actual class Encoding private constructor(public actual val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Encoding) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Encoding(name=$name)"
    }

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
        public actual fun <T> ofTelemetry(payload: T): Event<T> = IOSEventWrapper(payload, null, Priority.DEFAULT)
        public actual fun <T> ofTelemetry(code: Int, payload: T): Event<T> = IOSEventWrapper(payload, code, Priority.DEFAULT)
        public actual fun <T> ofUrgent(payload: T): Event<T> = IOSEventWrapper(payload, null, Priority.HIGHEST)
        public actual fun <T> ofUrgent(code: Int, payload: T): Event<T> = IOSEventWrapper(payload, code, Priority.HIGHEST)
    }
}

internal class IOSEventWrapper<T>(
    private val payload: T,
    private val code: Int?,
    private val priority: Priority
) : Event<T>() {
    override fun getPayload(): T = payload
    override fun getCode(): Int? = code
    override fun getPriority(): Priority = priority
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

public class IOSTransport<T> : Transport<T> {
    override fun send(event: Event<T>) {
        // No-op simulation
    }

    override fun schedule(event: Event<T>, callback: TransportScheduleCallback) {
        callback.onSchedule(null) // Mock successful callback
    }
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

public class IOSTransportFactory : TransportFactory {
    override fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        transformer: Transformer<T, ByteArray>
    ): Transport<T> = IOSTransport()

    override fun <T : Any> getTransport(
        name: String,
        payloadType: KClass<T>,
        encoding: Encoding,
        transformer: Transformer<T, ByteArray>
    ): Transport<T> = IOSTransport()
}
