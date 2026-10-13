package zone.ien.firebase.perf

public actual class FirebasePerformance private constructor(
    private val androidPerformance: com.google.firebase.perf.FirebasePerformance
) {
    public actual var isPerformanceCollectionEnabled: Boolean
        get() = androidPerformance.isPerformanceCollectionEnabled
        set(value) {
            androidPerformance.isPerformanceCollectionEnabled = value
        }

    public actual fun newTrace(traceName: String): Trace {
        return Trace(androidPerformance.newTrace(traceName))
    }

    public actual fun newHttpMetric(url: String, httpMethod: String): HttpMetric {
        return HttpMetric(androidPerformance.newHttpMetric(url, httpMethod))
    }

    public actual companion object {
        public actual val instance: FirebasePerformance
            get() = FirebasePerformance(com.google.firebase.perf.FirebasePerformance.getInstance())
    }
}
