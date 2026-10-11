package zone.ien.firebase.example.ui.test

import zone.ien.firebase.protobuf.Timestamp
import zone.ien.firebase.protobuf.Duration

public object WellKnownTypesTest {
    public fun verifyCompilation(): Boolean {
        // Instantiate and build Timestamp using expect-actual contract
        val timestamp = Timestamp.newBuilder()
            .setSeconds(1717200000L)
            .setNanos(123456)
            .build()

        // Instantiate and build Duration
        val duration = Duration.newBuilder()
            .setSeconds(3600L)
            .setNanos(999)
            .build()

        // Core assertions proving clean KMP data mapping
        val isTimestampValid = (timestamp.seconds == 1717200000L && timestamp.nanos == 123456)
        val isDurationValid = (duration.seconds == 3600L && duration.nanos == 999)

        return isTimestampValid && isDurationValid
    }
}
