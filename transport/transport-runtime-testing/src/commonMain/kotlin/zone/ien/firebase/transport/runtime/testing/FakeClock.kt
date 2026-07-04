package zone.ien.firebase.transport.runtime.testing

public class FakeClock(private var timeMillis: Long = 0L) {
    public fun advanceBy(millis: Long) {
        require(millis >= 0) { "Cannot advance clock by negative time" }
        timeMillis += millis
    }

    public fun getCurrentTime(): Long = timeMillis
}
