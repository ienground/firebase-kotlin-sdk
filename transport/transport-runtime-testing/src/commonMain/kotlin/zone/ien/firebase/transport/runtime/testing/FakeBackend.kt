package zone.ien.firebase.transport.runtime.testing

import zone.ien.firebase.transport.Event

public class FakeBackend {
    private val sentEvents = mutableListOf<Event<*>>()

    public fun send(event: Event<*>) {
        sentEvents.add(event)
    }

    public fun getSentEvents(): List<Event<*>> = sentEvents.toList()

    public fun getSentCount(): Int = sentEvents.size

    public fun clear() {
        sentEvents.clear()
    }
}
