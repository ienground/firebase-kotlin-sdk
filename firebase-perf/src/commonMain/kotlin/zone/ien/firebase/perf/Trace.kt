package zone.ien.firebase.perf

public expect class Trace {
    public fun start()
    public fun stop()
    public fun incrementMetric(metricName: String, incrementBy: Long)
    public fun putMetric(metricName: String, value: Long)
    public fun getLongMetric(metricName: String): Long
    public fun putAttribute(attribute: String, value: String)
    public fun getAttribute(attribute: String): String?
    public fun removeAttribute(attribute: String)
    public val attributes: Map<String, String>
}
