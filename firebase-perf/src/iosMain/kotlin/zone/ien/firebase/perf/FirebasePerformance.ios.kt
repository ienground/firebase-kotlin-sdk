package zone.ien.firebase.perf

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.perf.FIRPerformance

@OptIn(ExperimentalForeignApi::class)
public actual class FirebasePerformance private constructor(
    private val iosPerformance: FIRPerformance
) {
    public actual var isPerformanceCollectionEnabled: Boolean
        get() = iosPerformance.isDataCollectionEnabled()
        set(value) {
            iosPerformance.setDataCollectionEnabled(value)
        }

    public actual fun newTrace(traceName: String): Trace {
        val iosTrace = iosPerformance.traceWithName(traceName) ?: throw Exception("Trace creation failed")
        return Trace(iosTrace)
    }

    public actual fun newHttpMetric(url: String, httpMethod: String): HttpMetric {
        return HttpMetric(url, httpMethod)
    }

    public actual companion object {
        public actual val instance: FirebasePerformance
            get() = FirebasePerformance(FIRPerformance.sharedInstance())
    }
}
