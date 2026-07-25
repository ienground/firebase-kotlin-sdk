package zone.ien.firebase.perf

import kotlin.test.Test
import kotlin.test.assertEquals

class PerformanceApiContractTest {
    @Test
    fun 성능_측정_타입이_공통_API에_노출된다() {
        assertEquals("FirebasePerformance", FirebasePerformance::class.simpleName)
        assertEquals("Trace", Trace::class.simpleName)
        assertEquals("HttpMetric", HttpMetric::class.simpleName)
    }
}
