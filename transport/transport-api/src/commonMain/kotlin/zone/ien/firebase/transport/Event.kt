package zone.ien.firebase.transport

public expect abstract class Event<T> {
    public abstract fun getPayload(): T
    public abstract fun getCode(): Int?
    public abstract fun getPriority(): Priority

    public companion object {
        public fun <T> ofTelemetry(payload: T): Event<T>
        public fun <T> ofTelemetry(code: Int, payload: T): Event<T>
        public fun <T> ofUrgent(payload: T): Event<T>
        public fun <T> ofUrgent(code: Int, payload: T): Event<T>
    }
}
