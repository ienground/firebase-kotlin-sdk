package zone.ien.firebase.transport.runtime.testing

import zone.ien.firebase.transport.Event

public object TestTransportEventBuilder {
    public fun <T> buildTelemetry(payload: T, code: Int? = null): Event<T> {
        return if (code != null) {
            Event.ofTelemetry(code, payload)
        } else {
            Event.ofTelemetry(payload)
        }
    }

    public fun <T> buildUrgent(payload: T, code: Int? = null): Event<T> {
        return if (code != null) {
            Event.ofUrgent(code, payload)
        } else {
            Event.ofUrgent(payload)
        }
    }
}
