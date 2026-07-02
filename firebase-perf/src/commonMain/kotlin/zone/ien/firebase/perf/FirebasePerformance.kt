package zone.ien.firebase.perf

public expect class FirebasePerformance {
    public var isPerformanceCollectionEnabled: Boolean
    public fun newTrace(traceName: String): Trace
    public fun newHttpMetric(url: String, httpMethod: String): HttpMetric

    public companion object {
        public val instance: FirebasePerformance
    }
}
