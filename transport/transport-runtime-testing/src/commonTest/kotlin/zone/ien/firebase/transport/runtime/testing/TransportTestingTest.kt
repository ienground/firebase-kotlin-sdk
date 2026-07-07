package zone.ien.firebase.transport.runtime.testing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TransportTestingTest {

    @Test
    fun testFakeClock() {
        val clock = FakeClock(1000L)
        assertEquals(1000L, clock.getCurrentTime())
        clock.advanceBy(500L)
        assertEquals(1500L, clock.getCurrentTime())
    }

    @Test
    fun testFakeClockNegativeAdvance() {
        val clock = FakeClock(1000L)
        kotlin.test.assertFailsWith<IllegalArgumentException> {
            clock.advanceBy(-500L)
        }
    }

    @Test
    fun testFakeEventStore() {
        val store = FakeEventStore()
        assertEquals(0, store.getCount())

        val event = TestTransportEventBuilder.buildTelemetry("event-payload-123")
        store.persist(event)

        assertEquals(1, store.getCount())
        assertEquals("event-payload-123", store.getEvents().first().getPayload())
        
        store.clear()
        assertEquals(0, store.getCount())
    }

    @Test
    fun testFakeBackend() {
        val backend = FakeBackend()
        assertEquals(0, backend.getSentCount())

        val event = TestTransportEventBuilder.buildUrgent("urgent-payload-456", code = 77)
        backend.send(event)

        assertEquals(1, backend.getSentCount())
        val sent = backend.getSentEvents().first()
        assertEquals("urgent-payload-456", sent.getPayload())
        assertEquals(77, sent.getCode())

        backend.clear()
        assertEquals(0, backend.getSentCount())
    }
}
