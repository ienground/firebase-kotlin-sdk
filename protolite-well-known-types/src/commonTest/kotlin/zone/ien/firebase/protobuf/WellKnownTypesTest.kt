package zone.ien.firebase.protobuf

import kotlin.test.Test
import kotlin.test.assertEquals

class WellKnownTypesTest {

    @Test
    fun testTimestampBuilder() {
        val timestamp = Timestamp.newBuilder()
            .setSeconds(987654321L)
            .setNanos(456)
            .build()

        assertEquals(987654321L, timestamp.seconds)
        assertEquals(456, timestamp.nanos)
    }

    @Test
    fun testDurationBuilder() {
        val duration = Duration.newBuilder()
            .setSeconds(120L)
            .setNanos(789)
            .build()

        assertEquals(120L, duration.seconds)
        assertEquals(789, duration.nanos)
    }
}
