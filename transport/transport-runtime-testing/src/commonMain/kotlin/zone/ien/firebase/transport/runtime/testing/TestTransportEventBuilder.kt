package zone.ien.firebase.transport.runtime.testing

import zone.ien.firebase.transport.Event

public object TestTransportEventBuilder {
    public fun <T> buildTelemetry(payload: T, code: Int? = null): Event<T> =
        code?.let { Event.ofTelemetry(it, payload) } ?: Event.ofTelemetry(payload)

    public fun <T> buildUrgent(payload: T, code: Int? = null): Event<T> =
        code?.let { Event.ofUrgent(it, payload) } ?: Event.ofUrgent(payload)
}
