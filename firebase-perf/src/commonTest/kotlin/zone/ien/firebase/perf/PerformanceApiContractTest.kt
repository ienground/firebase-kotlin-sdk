package zone.ien.firebase.perf

import kotlin.test.Test
import kotlin.test.assertEquals

class PerformanceApiContractTest {
    @Test
    fun testPerformanceTypesExposedInCommonApi() {
        assertEquals("FirebasePerformance", FirebasePerformance::class.simpleName)
        assertEquals("Trace", Trace::class.simpleName)
        assertEquals("HttpMetric", HttpMetric::class.simpleName)
    }
}
