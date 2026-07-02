package zone.ien.firebase.perf

public actual class Trace internal constructor(
    private val androidTrace: com.google.firebase.perf.metrics.Trace
) {
    public actual fun start() {
        androidTrace.start()
    }

    public actual fun stop() {
        androidTrace.stop()
    }

    public actual fun incrementMetric(metricName: String, incrementBy: Long) {
        androidTrace.incrementMetric(metricName, incrementBy)
    }

    public actual fun putMetric(metricName: String, value: Long) {
        androidTrace.putMetric(metricName, value)
    }

    public actual fun getLongMetric(metricName: String): Long {
        return androidTrace.getLongMetric(metricName)
    }

    public actual fun putAttribute(attribute: String, value: String) {
        androidTrace.putAttribute(attribute, value)
    }

    public actual fun getAttribute(attribute: String): String? {
        return androidTrace.getAttribute(attribute)
    }

    public actual fun removeAttribute(attribute: String) {
        androidTrace.removeAttribute(attribute)
    }

    public actual val attributes: Map<String, String>
        get() = androidTrace.attributes
}
