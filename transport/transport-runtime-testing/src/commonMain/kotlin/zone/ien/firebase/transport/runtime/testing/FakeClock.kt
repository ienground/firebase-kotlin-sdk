package zone.ien.firebase.transport.runtime.testing

public class FakeClock(initialTimeMillis: Long = 0L) {
    @kotlin.concurrent.Volatile
    private var timeMillis: Long = initialTimeMillis
    public fun advanceBy(millis: Long) {
        require(millis >= 0) { "Cannot advance clock by negative time" }
        timeMillis += millis
    }
    public fun getCurrentTime(): Long = timeMillis
}