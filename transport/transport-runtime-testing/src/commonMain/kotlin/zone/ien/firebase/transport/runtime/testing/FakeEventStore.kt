package zone.ien.firebase.transport.runtime.testing

import zone.ien.firebase.transport.Event

public class FakeEventStore {
    private val events = mutableListOf<Event<*>>()

    public fun persist(event: Event<*>) {
        events.add(event)
    }

    public fun getEvents(): List<Event<*>> = events.toList()

    public fun getCount(): Int = events.size

    public fun clear() {
        events.clear()
    }
}
